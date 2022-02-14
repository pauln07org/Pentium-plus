 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.entity.player;

import net.minecraft.item.ItemStack;

public class StoppedUsingItemEvent {
    private static final StoppedUsingItemEvent INSTANCE = new StoppedUsingItemEvent();

    public ItemStack itemStack;

    public static StoppedUsingItemEvent get(ItemStack itemStack) {
        INSTANCE.itemStack = itemStack;
        return INSTANCE;
    }
}
