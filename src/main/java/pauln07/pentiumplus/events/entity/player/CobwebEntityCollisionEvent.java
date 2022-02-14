 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.entity.player;

import pauln07.pentiumplus.events.Cancellable;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CobwebEntityCollisionEvent extends Cancellable {
    private static final CobwebEntityCollisionEvent INSTANCE = new CobwebEntityCollisionEvent();

    public BlockState state;
    public BlockPos blockPos;

    public static CobwebEntityCollisionEvent get(BlockState state, BlockPos blockPos) {
        INSTANCE.setCancelled(false);
        INSTANCE.state = state;
        INSTANCE.blockPos = blockPos;
        return INSTANCE;
    }
}
