 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.render;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.meteor.MouseScrollEvent;
import pauln07.pentiumplus.events.render.GetFovEvent;
import pauln07.pentiumplus.events.render.Render3DEvent;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.DoubleSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.MathHelper;

public class Zoom extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> zoom = sgGeneral.add(new DoubleSetting.Builder()
            .name("zoom")
            .description("How much to zoom.")
            .defaultValue(6)
            .min(1)
            .build()
    );

    private final Setting<Double> scrollSensitivity = sgGeneral.add(new DoubleSetting.Builder()
            .name("scroll-sensitivity")
            .description("Allows you to change zoom value using scroll wheel. 0 to disable.")
            .defaultValue(1)
            .min(0)
            .build()
    );

    private final Setting<Boolean> smooth = sgGeneral.add(new BoolSetting.Builder()
        .name("smooth")
        .description("Smooth transition.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> cinematic = sgGeneral.add(new BoolSetting.Builder()
            .name("cinematic")
            .description("Enables cinematic camera.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> renderHands = sgGeneral.add(new BoolSetting.Builder()
        .name("show-hands")
        .description("Whether or not to render your hands.")
        .defaultValue(false)
        .build()
    );

    private boolean enabled;
    private boolean preCinematic;
    private double preMouseSensitivity;
    private double value;
    private double lastFov;
    private double time;

    public Zoom() {
        super(Categories.Render, "zoom", "Zooms your view.");
        autoSubscribe = false;
    }

    @Override
    public void onActivate() {
        if (!enabled) {
            preCinematic = mc.options.smoothCameraEnabled;
            preMouseSensitivity = mc.options.mouseSensitivity;
            value = zoom.get();
            lastFov = mc.options.fov;
            time = 0.001;

            PentiumPlus.EVENT_BUS.subscribe(this);
            enabled = true;
        }
    }

    public void onStop() {
        mc.options.smoothCameraEnabled = preCinematic;
        mc.options.mouseSensitivity = preMouseSensitivity;

        mc.worldRenderer.scheduleTerrainUpdate();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        mc.options.smoothCameraEnabled = cinematic.get();

        if (!cinematic.get()) {
            mc.options.mouseSensitivity = preMouseSensitivity / Math.max(value() * 0.5, 1);
        }

        if (time == 0) {
            PentiumPlus.EVENT_BUS.unsubscribe(this);
            enabled = false;

            onStop();
        }
    }

    @EventHandler
    private void onMouseScroll(MouseScrollEvent event) {
        if (scrollSensitivity.get() > 0 && isActive()) {
            value += event.value * 0.25 * (scrollSensitivity.get() * value);
            if (value < 1) value = 1;

            event.cancel();
        }
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (!smooth.get()) {
            time = isActive() ? 1 : 0;
            return;
        }

        if (isActive()) time += event.frameTime * 5;
        else time -= event.frameTime * 5;

        time = Utils.clamp(time, 0, 1);
    }

    @EventHandler
    private void onGetFov(GetFovEvent event) {
        event.fov /= value();

        if (lastFov != event.fov) mc.worldRenderer.scheduleTerrainUpdate();
        lastFov = event.fov;
    }

    private double value() {
        double delta = time < 0.5 ? 4 * time * time * time : 1 - Math.pow(-2 * time + 2, 3) / 2; // Ease in out cubic
        return MathHelper.lerp(delta, 1, value);
    }

    public boolean renderHands() {
        return !isActive() || renderHands.get();
    }
}
