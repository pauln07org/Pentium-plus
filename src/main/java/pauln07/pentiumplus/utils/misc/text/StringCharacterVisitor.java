 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.utils.misc.text;

import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;

public class StringCharacterVisitor implements CharacterVisitor {

    public StringBuilder result = new StringBuilder();

    @Override
    public boolean accept(int index, Style style, int j) {
        result.append((char)j);
        return true;
    }
}
