 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.utils.Cell;
import pauln07.pentiumplus.gui.widgets.WWidget;
import pauln07.pentiumplus.gui.widgets.containers.WWindow;
import pauln07.pentiumplus.systems.modules.AllowedModules;

public abstract class WindowTabScreen extends TabScreen {
    protected final WWindow window;

    public WindowTabScreen(GuiTheme theme, Tab tab) {
        super(theme, tab);
        
        if (AllowedModules.isValidTab(tab)) {
        	window = super.add(theme.window(tab.name)).center().widget();        	
        } else {
        	window = null;        	
        }
    }

    @Override
    public <W extends WWidget> Cell<W> add(W widget) {
        return window.add(widget);
    }

    @Override
    public void clear() {
        window.clear();
    }
}
