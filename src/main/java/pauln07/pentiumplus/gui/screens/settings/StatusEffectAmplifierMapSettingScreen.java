 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.screens.settings;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.WindowScreen;
import pauln07.pentiumplus.gui.widgets.containers.WTable;
import pauln07.pentiumplus.gui.widgets.input.WIntEdit;
import pauln07.pentiumplus.gui.widgets.input.WTextBox;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.utils.misc.Names;
import net.minecraft.entity.effect.StatusEffect;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatusEffectAmplifierMapSettingScreen extends WindowScreen {
    private final Setting<Object2IntMap<StatusEffect>> setting;

    private WTable table;

    private WTextBox filter;
    private String filterText = "";

    public StatusEffectAmplifierMapSettingScreen(GuiTheme theme, Setting<Object2IntMap<StatusEffect>> setting) {
        super(theme, "Modify Amplifiers");

        this.setting = setting;

        initWidgets();
    }

    @Override
    public void initWidgets() {
        filter = add(theme.textBox("")).minWidth(400).expandX().widget();
        filter.setFocused(true);
        filter.action = () -> {
            filterText = filter.get().trim();

            table.clear();
            initTable();
        };

        table = add(theme.table()).expandX().widget();
        initTable();
    }

    private void initTable() {
        List<StatusEffect> statusEffects = new ArrayList<>(setting.get().keySet());
        statusEffects.sort(Comparator.comparing(Names::get));

        for (StatusEffect statusEffect : statusEffects) {
            String name = Names.get(statusEffect);
            if (!StringUtils.containsIgnoreCase(name, filterText)) continue;

            table.add(theme.label(name)).expandCellX();

            WIntEdit level = theme.intEdit(setting.get().getInt(statusEffect), 0, Integer.MAX_VALUE, true);
            level.action = () -> {
                setting.get().put(statusEffect, level.get());
                setting.onChanged();
            };

            table.add(level).minWidth(50);
            table.row();
        }
    }
}
