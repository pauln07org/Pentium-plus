 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.settings;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.WidgetScreen;
import pauln07.pentiumplus.utils.misc.IChangeable;
import pauln07.pentiumplus.utils.misc.ICopyable;
import pauln07.pentiumplus.utils.misc.ISerializable;
import net.minecraft.block.Block;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IBlockData<T>> {
    WidgetScreen createScreen(GuiTheme theme, Block block, BlockDataSetting<T> setting);
}
