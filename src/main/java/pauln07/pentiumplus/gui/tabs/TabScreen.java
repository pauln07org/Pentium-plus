 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.WidgetScreen;
import pauln07.pentiumplus.gui.utils.Cell;
import pauln07.pentiumplus.gui.widgets.WWidget;
import pauln07.pentiumplus.systems.modules.AllowedModules;

public abstract class TabScreen extends WidgetScreen {
    public final Tab tab;

    public TabScreen(GuiTheme theme, Tab tab) {
    	super(theme, tab.name);
    	if (AllowedModules.isValidTab(tab)) {
    		this.tab = tab;
    	} else {
    		this.tab = null;
    	}
    }

    public <T extends WWidget> Cell<T> addDirect(T widget) {
    	System.out.println("Widget: " + widget + " tab: "+this.tab);
        return super.add(widget);
    }
}
