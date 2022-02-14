 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.hud.modules;

import pauln07.pentiumplus.settings.EnumSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.hud.HUD;
import pauln07.pentiumplus.utils.Utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeHud extends DoubleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Type> timeType = sgGeneral.add(new EnumSetting.Builder<Type>()
        .name("type")
        .description("Which time to use.")
        .defaultValue(Type.Game)
        .build()
    );

    public TimeHud(HUD hud) {
        super(hud, "time", "Displays the world time.", "Time: ");
    }

    @Override
    protected String getRight() {
        return switch (timeType.get()) {
            case Game -> isInEditor() ? "00:00" : Utils.getWorldTime();
            case Local -> LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        };
    }

    public enum Type {
        Local,
        Game
    }
}
