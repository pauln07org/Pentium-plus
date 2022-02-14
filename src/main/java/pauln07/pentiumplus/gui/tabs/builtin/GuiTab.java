 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs.builtin;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.GuiThemes;
import pauln07.pentiumplus.gui.tabs.Tab;
import pauln07.pentiumplus.gui.tabs.TabScreen;
import pauln07.pentiumplus.gui.tabs.WindowTabScreen;
import pauln07.pentiumplus.gui.widgets.containers.WTable;
import pauln07.pentiumplus.gui.widgets.input.WDropdown;
import pauln07.pentiumplus.utils.misc.NbtUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class GuiTab extends Tab {
    public GuiTab() {
        super("GUI");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new GuiScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof GuiScreen;
    }

    private static class GuiScreen extends WindowTabScreen {
        public GuiScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            theme.settings.onActivated();
        }

        @Override
        public void initWidgets() {
            WTable table = add(theme.table()).expandX().widget();

            table.add(theme.label("Theme:"));
            WDropdown<String> themeW = table.add(theme.dropdown(GuiThemes.getNames(), GuiThemes.get().name)).widget();
            themeW.action = () -> {
                GuiThemes.select(themeW.get());

                mc.setScreen(null);
                tab.openScreen(GuiThemes.get());
            };

            add(theme.settings(theme.settings)).expandX();
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(theme.name + " GUI Theme", theme.toTag());
        }

        @Override
        public boolean fromClipboard() {
            NbtCompound clipboard = NbtUtils.fromClipboard(theme.toTag());

            if (clipboard != null) {
                theme.fromTag(clipboard);
                return true;
            }

            return false;
        }
    }
}
