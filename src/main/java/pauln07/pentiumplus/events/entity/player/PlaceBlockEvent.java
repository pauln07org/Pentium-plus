 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.entity.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockEvent {
    private static final PlaceBlockEvent INSTANCE = new PlaceBlockEvent();

    public BlockPos blockPos;
    public Block block;

    public static PlaceBlockEvent get(BlockPos blockPos, Block block) {
        INSTANCE.blockPos = blockPos;
        INSTANCE.block = block;
        return INSTANCE;
    }
}
