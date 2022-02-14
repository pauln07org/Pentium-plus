 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.render;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.EnumSetting;
import pauln07.pentiumplus.settings.IntSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.systems.modules.Modules;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {
    public enum Mode {
        Gamma,
        Luminance
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("The mode to use for Fullbright.")
            .defaultValue(Mode.Luminance)
            .onChanged(mode -> {
                if (mode == Mode.Luminance) {
                    mc.options.gamma = StaticListener.prevGamma;
                }
            })
            .build()
    );

    private final Setting<Integer> minimumLightLevel = sgGeneral.add(new IntSetting.Builder()
        .name("minimum-light-level")
        .description("Minimum light level when using Luminance mode.")
        .visible(() -> mode.get() == Mode.Luminance)
        .defaultValue(8)
        .onChanged(integer -> {
            if (mc.worldRenderer != null) mc.worldRenderer.reload();
        })
        .range(0, 15)
        .sliderMax(15)
        .build()
    );

    public Fullbright() {
        super(Categories.Render, "fullbright", "Lights up your world!");

        PentiumPlus.EVENT_BUS.subscribe(StaticListener.class);
    }

    @Override
    public void onActivate() {
        enable();

        if (mode.get() == Mode.Luminance) mc.worldRenderer.reload();
    }

    @Override
    public void onDeactivate() {
        disable();

        if (mode.get() == Mode.Luminance) mc.worldRenderer.reload();
    }

    public int getMinimumLightLevel() {
        if (!isActive() || mode.get() != Mode.Luminance) return 0;
        return minimumLightLevel.get();
    }

    public static boolean isEnabled() {
        return StaticListener.timesEnabled > 0;
    }

    public static void enable() {
        StaticListener.timesEnabled++;
    }

    public static void disable() {
        StaticListener.timesEnabled--;
    }

    private static class StaticListener {
        private static final MinecraftClient mc = MinecraftClient.getInstance();
        private static final Fullbright fullbright = Modules.get().get(Fullbright.class);

        private static int timesEnabled;
        private static int lastTimesEnabled;

        private static double prevGamma = mc.options.gamma;

        @EventHandler
        private static void onTick(TickEvent.Post event) {
            if (timesEnabled > 0 && lastTimesEnabled == 0) {
                prevGamma = mc.options.gamma;
            } else if (timesEnabled == 0 && lastTimesEnabled > 0) {
                if (fullbright.mode.get() == Mode.Gamma) {
                    mc.options.gamma = prevGamma == 16 ? 1 : prevGamma;
                }
            }

            if (timesEnabled > 0) {
                if (fullbright.mode.get() == Mode.Gamma) {
                    mc.options.gamma = 16;
                }
            }

            lastTimesEnabled = timesEnabled;
        }
    }
}
