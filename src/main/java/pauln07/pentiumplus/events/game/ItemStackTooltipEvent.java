 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.game;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ItemStackTooltipEvent {
    private static final ItemStackTooltipEvent INSTANCE = new ItemStackTooltipEvent();

    public ItemStack itemStack;
    public List<Text> list;

    public static ItemStackTooltipEvent get(ItemStack itemStack, List<Text> list) {
        INSTANCE.itemStack = itemStack;
        INSTANCE.list = list;
        return INSTANCE;
    }
}
