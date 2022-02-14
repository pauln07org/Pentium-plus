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
import pauln07.pentiumplus.utils.player.PlayerUtils;
import pauln07.pentiumplus.utils.player.Rotations;
import pauln07.pentiumplus.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AntiBed extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> placeStringTop = sgGeneral.add(new BoolSetting.Builder()
        .name("place-string-top")
        .description("Places string above you.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> placeStringMiddle = sgGeneral.add(new BoolSetting.Builder()
        .name("place-string-middle")
        .description("Places string in your upper hitbox.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> placeStringBottom = sgGeneral.add(new BoolSetting.Builder()
        .name("place-string-bottom")
        .description("Places string at your feet.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> onlyInHole = sgGeneral.add(new BoolSetting.Builder()
        .name("only-in-hole")
        .description("Only functions when you are standing in a hole.")
        .defaultValue(true)
        .build()
    );

    private boolean breaking;

    public AntiBed() {
        super(Categories.Combat, "anti-bed", "Places string to prevent beds being placed on you.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (onlyInHole.get() && !PlayerUtils.isInHole(true)) return;

        // Checking for and maybe breaking bed
        BlockPos head = mc.player.getBlockPos().up();

        if (mc.world.getBlockState(head).getBlock() instanceof BedBlock && !breaking) {
            Rotations.rotate(Rotations.getYaw(head), Rotations.getPitch(head), 50, () -> sendMinePackets(head));
            breaking = true;
        } else if (breaking) {
            Rotations.rotate(Rotations.getYaw(head), Rotations.getPitch(head), 50, () -> sendStopPackets(head));
            breaking = false;
        }

        // String placement
        if (placeStringTop.get()) place(mc.player.getBlockPos().up(2));
        if (placeStringMiddle.get()) place(mc.player.getBlockPos().up(1));
        if (placeStringBottom.get()) place(mc.player.getBlockPos());
    }

    private void place(BlockPos blockPos) {
        if (mc.world.getBlockState(blockPos).getBlock().asItem() != Items.STRING) {
            BlockUtils.place(blockPos, InvUtils.findInHotbar(Items.STRING), 50, false);
        }
    }

    private void sendMinePackets(BlockPos blockPos) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
    }

    private void sendStopPackets(BlockPos blockPos) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, blockPos, Direction.UP));
        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }
}