 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.systems.modules.AllowedModules;
import net.minecraft.client.gui.screen.Screen;

import static pauln07.pentiumplus.PentiumPlus.mc;

public abstract class Tab {
    public final String name;

    public Tab(String name) {
        this.name = name;
    }

    public void openScreen(GuiTheme theme) {
    	if (AllowedModules.isValidTab(this)) {
    		TabScreen screen = this.createScreen(theme);
    		screen.addDirect(theme.topBar()).top().centerX();
    		mc.setScreen(screen);
    	}
    }

    public abstract TabScreen createScreen(GuiTheme theme);

    public abstract boolean isScreen(Screen screen);
}
