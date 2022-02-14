 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.tabs.builtin;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.renderer.GuiRenderer;
import pauln07.pentiumplus.gui.screens.HudEditorScreen;
import pauln07.pentiumplus.gui.tabs.Tab;
import pauln07.pentiumplus.gui.tabs.TabScreen;
import pauln07.pentiumplus.gui.tabs.WindowTabScreen;
import pauln07.pentiumplus.gui.widgets.containers.WHorizontalList;
import pauln07.pentiumplus.gui.widgets.pressable.WButton;
import pauln07.pentiumplus.gui.widgets.pressable.WCheckbox;
import pauln07.pentiumplus.systems.Systems;
import pauln07.pentiumplus.systems.hud.HUD;
import pauln07.pentiumplus.utils.misc.NbtUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class HudTab extends Tab {
    public HudTab() {
        super("HUD");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new HudScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof HudScreen;
    }

    public static class HudScreen extends WindowTabScreen {
        private final HUD hud;

        public HudScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            hud = Systems.get(HUD.class);
            hud.settings.onActivated();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(hud.settings)).expandX();

            add(theme.horizontalSeparator()).expandX();

            WButton openEditor = add(theme.button("Edit")).expandX().widget();
            openEditor.action = () -> mc.setScreen(new HudEditorScreen(theme, this));

            WButton resetHud = add(theme.button("Reset")).expandX().widget();
            resetHud.action = hud.reset;

            add(theme.horizontalSeparator()).expandX();

            WHorizontalList bottom = add(theme.horizontalList()).expandX().widget();

            bottom.add(theme.label("Active: "));
            WCheckbox active = bottom.add(theme.checkbox(hud.active)).expandCellX().widget();
            active.action = () -> hud.active = active.checked;

            WButton resetSettings = bottom.add(theme.button(GuiRenderer.RESET)).widget();
            resetSettings.action = hud.settings::reset;
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard("hud-settings", hud.settings.toTag());
        }

        @Override
        public boolean fromClipboard() {
            NbtCompound clipboard = NbtUtils.fromClipboard(hud.settings.toTag());

            if (clipboard != null) {
                hud.settings.fromTag(clipboard);
                return true;
            }

            return false;
        }
    }
}
