 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.movement;

import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;

public class AntiLevitation extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> applyGravity = sgGeneral.add(new BoolSetting.Builder()
            .name("gravity")
            .description("Applies gravity.")
            .defaultValue(false)
            .build()
    );

    public AntiLevitation() {
        super(Categories.Movement, "anti-levitation", "Prevents the levitation effect from working.");
    }

    public boolean isApplyGravity() {
        return applyGravity.get();
    }
}
