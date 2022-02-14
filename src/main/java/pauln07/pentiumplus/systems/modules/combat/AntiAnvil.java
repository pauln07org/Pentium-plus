 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.combat;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.player.InvUtils;
import pauln07.pentiumplus.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class AntiAnvil extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> swing = sgGeneral.add(new BoolSetting.Builder()
        .name("swing")
        .description("Swings your hand client-side when placing.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Makes you rotate when placing.")
        .defaultValue(true)
        .build()
    );

    public AntiAnvil() {
        super(Categories.Combat, "anti-anvil", "Automatically prevents Auto Anvil by placing between you and the anvil.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        for (int i = 0; i <= mc.interactionManager.getReachDistance(); i++) {
            BlockPos pos = mc.player.getBlockPos().add(0, i + 3, 0);

            if (mc.world.getBlockState(pos).getBlock() == Blocks.ANVIL && mc.world.getBlockState(pos.down()).isAir()) {
                if (BlockUtils.place(pos.down(), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 15, swing.get(), true))
                    break;
            }
        }
    }
}
