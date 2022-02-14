 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs;

import pauln07.pentiumplus.gui.tabs.builtin.*;
import pauln07.pentiumplus.systems.modules.AllowedModules;
import pauln07.pentiumplus.utils.Init;
import pauln07.pentiumplus.utils.InitStage;

import java.util.ArrayList;
import java.util.List;

public class Tabs {
    private static final List<Tab> tabs = new ArrayList<>();

    @Init(stage = InitStage.Pre)
    public static void init() {
        add(new ModulesTab());
        add(new ConfigTab());
        add(new GuiTab());
        add(new HudTab());
        add(new FriendsTab());
        add(new MacrosTab());
        add(new ProfilesTab());
        add(new BaritoneTab());
    }

    public static void add(Tab tab) {
    	if (AllowedModules.isValidTab(tab)) {
    		tabs.add(tab);    		
    	}
    }

    public static List<Tab> get() {
        return tabs;
    }
}
