 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.movement;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Sprint extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> whenStationary = sgGeneral.add(new BoolSetting.Builder()
            .name("when-stationary")
            .description("Continues sprinting even if you do not move.")
            .defaultValue(true)
            .build()
    );

    public Sprint() {
        super(Categories.Movement, "sprint", "Automatically sprints.");
    }

    @Override
    public void onDeactivate() {
        mc.player.setSprinting(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.forwardSpeed > 0 && !whenStationary.get()) {
            mc.player.setSprinting(true);
        } else if (whenStationary.get()) {
            mc.player.setSprinting(true);
        }
    }
}
