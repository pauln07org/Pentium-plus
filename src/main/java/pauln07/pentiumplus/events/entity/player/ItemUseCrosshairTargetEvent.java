 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.entity.player;

import net.minecraft.util.hit.HitResult;

public class ItemUseCrosshairTargetEvent {
    private static final ItemUseCrosshairTargetEvent INSTANCE = new ItemUseCrosshairTargetEvent();

    public HitResult target;

    public static ItemUseCrosshairTargetEvent get(HitResult target) {
        INSTANCE.target = target;
        return INSTANCE;
    }
}
