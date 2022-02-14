 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.combat;

import pauln07.pentiumplus.events.game.OpenScreenEvent;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.entity.EntityUtils;
import pauln07.pentiumplus.utils.entity.SortPriority;
import pauln07.pentiumplus.utils.entity.TargetUtils;
import pauln07.pentiumplus.utils.player.FindItemResult;
import pauln07.pentiumplus.utils.player.InvUtils;
import pauln07.pentiumplus.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class AutoAnvil extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("target-range")
        .description("The radius in which players get targeted.")
        .defaultValue(4)
        .min(0)
        .sliderMax(5)
        .build()
    );

    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>()
        .name("target-priority")
        .description("How to select the player to target.")
        .defaultValue(SortPriority.LowestHealth)
        .build()
    );

    private final Setting<Integer> height = sgGeneral.add(new IntSetting.Builder()
        .name("height")
        .description("The height to place anvils at.")
        .defaultValue(2)
        .range(0, 5)
        .sliderMax(5)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay in between anvil placements.")
        .defaultValue(10)
        .min(0)
        .sliderMax(50)
        .build()
    );

    private final Setting<Boolean> placeButton = sgGeneral.add(new BoolSetting.Builder()
        .name("place-at-feet")
        .description("Automatically places a button or pressure plate at the targets feet to break the anvils.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> multiPlace = sgGeneral.add(new BoolSetting.Builder()
        .name("multi-place")
        .description("Places multiple anvils at once..")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> toggleOnBreak = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-on-break")
        .description("Toggles when the target's helmet slot is empty.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Automatically rotates towards the position anvils/pressure plates/buttons are placed.")
        .defaultValue(true)
        .build()
    );

    private PlayerEntity target;
    private int timer;

    public AutoAnvil() {
        super(Categories.Combat, "auto-anvil", "Automatically places anvils above players to destroy helmets.");
    }

    @Override
    public void onActivate() {
        timer = 0;
        target = null;
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof AnvilScreen) event.cancel();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        // Head check
        if (toggleOnBreak.get() && target != null && target.getInventory().getArmorStack(3).isEmpty()) {
            error("Target head slot is empty... disabling.");
            toggle();
            return;
        }

        // Check distance + alive
        if (TargetUtils.isBadTarget(target, range.get()))
            target = TargetUtils.getPlayerTarget(range.get(), priority.get());
        if (TargetUtils.isBadTarget(target, range.get())) return;

        if (placeButton.get()) {
            FindItemResult floorBlock = InvUtils.findInHotbar(itemStack -> Block.getBlockFromItem(itemStack.getItem()) instanceof AbstractPressurePlateBlock || Block.getBlockFromItem(itemStack.getItem()) instanceof AbstractButtonBlock);
            BlockUtils.place(target.getBlockPos(), floorBlock, rotate.get(), 0, false);
        }

        if (timer >= delay.get()) {
            timer = 0;

            FindItemResult anvil = InvUtils.findInHotbar(itemStack -> Block.getBlockFromItem(itemStack.getItem()) instanceof AnvilBlock);
            if (!anvil.found()) return;

            for (int i = height.get(); i > 1; i--) {
                BlockPos blockPos = target.getBlockPos().up().add(0, i, 0);

                for (int j = 0; j < i; j++) {
                    if (!mc.world.getBlockState(target.getBlockPos().up(j + 1)).getMaterial().isReplaceable()) {
                        break;
                    }
                }

                if (BlockUtils.place(blockPos, anvil, rotate.get(), 0) && !multiPlace.get()) break;
            }
        } else {
            timer++;
        }
    }

    @Override
    public String getInfoString() {
        return EntityUtils.getName(target);
    }
}
