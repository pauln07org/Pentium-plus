 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.player;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.DoubleSetting;
import pauln07.pentiumplus.settings.EnumSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Rotation extends Module {
    private final SettingGroup sgYaw = settings.createGroup("Yaw");
    private final SettingGroup sgPitch = settings.createGroup("Pitch");

    // Yaw

    private final Setting<LockMode> yawLockMode = sgYaw.add(new EnumSetting.Builder<LockMode>()
        .name("yaw-lock-mode")
        .description("The way in which your yaw is locked.")
        .defaultValue(LockMode.Simple)
        .build()
    );

    private final Setting<Double> yawAngle = sgYaw.add(new DoubleSetting.Builder()
        .name("yaw-angle")
        .description("Yaw angle in degrees.")
        .defaultValue(0)
        .sliderMax(360)
        .max(360)
        .build()
    );

    // Pitch

    private final Setting<LockMode> pitchLockMode = sgPitch.add(new EnumSetting.Builder<LockMode>()
        .name("pitch-lock-mode")
        .description("The way in which your pitch is locked.")
        .defaultValue(LockMode.Simple)
        .build()
    );

    private final Setting<Double> pitchAngle = sgPitch.add(new DoubleSetting.Builder()
        .name("pitch-angle")
        .description("Pitch angle in degrees.")
        .defaultValue(0)
        .range(-90, 90)
        .sliderRange(-90, 90)
        .build()
    );

    public Rotation() {
        super(Categories.Player, "rotation", "Changes/locks your yaw and pitch.");
    }

    @Override
    public void onActivate() {
        onTick(null);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (yawLockMode.get()) {
            case Simple -> setYawAngle(yawAngle.get().floatValue());
            case Smart  -> setYawAngle(getSmartYawDirection());
        }

        switch (pitchLockMode.get()) {
            case Simple -> mc.player.setPitch(pitchAngle.get().floatValue());
            case Smart  -> mc.player.setPitch(getSmartPitchDirection());
        }
    }

    private float getSmartYawDirection() {
        return Math.round((mc.player.getYaw() + 1f) / 45f) * 45f;
    }

    private float getSmartPitchDirection() {
        return Math.round((mc.player.getPitch() + 1f) / 30f) * 30f;
    }

    private void setYawAngle(float yawAngle) {
        mc.player.setYaw(yawAngle);
        mc.player.headYaw = yawAngle;
        mc.player.bodyYaw = yawAngle;
    }

    public enum LockMode {
        Smart,
        Simple,
        None
    }
}
