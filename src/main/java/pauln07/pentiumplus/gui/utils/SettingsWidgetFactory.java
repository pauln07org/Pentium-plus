 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.utils;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.widgets.WWidget;
import pauln07.pentiumplus.settings.Settings;

public interface SettingsWidgetFactory {
    WWidget create(GuiTheme theme, Settings settings, String filter);
}
