 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.player;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.mixin.StatusEffectInstanceAccessor;
import pauln07.pentiumplus.settings.DoubleSetting;
import pauln07.pentiumplus.settings.EnumSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.effect.StatusEffectInstance;

import static net.minecraft.entity.effect.StatusEffects.HASTE;

public class SpeedMine extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .defaultValue(Mode.Normal)
            .build()
    );
    public final Setting<Double> modifier = sgGeneral.add(new DoubleSetting.Builder()
            .name("modifier")
            .description("Mining speed modifier. An additional value of 0.2 is equivalent to one haste level (1.2 = haste 1).")
            .defaultValue(1.4)
            .min(0)
            .build()
    );

    public SpeedMine() {
        super(Categories.Player, "speed-mine", "Allows you to quickly mine blocks.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mode.get() == Mode.Normal) return;

        int amplifier = mode.get() == Mode.Haste2 ? 1 : 0;

        if (!mc.player.hasStatusEffect(HASTE)) {
            mc.player.addStatusEffect(new StatusEffectInstance(HASTE, 255, amplifier, false, false, false));
        }

        StatusEffectInstance effect = mc.player.getStatusEffect(HASTE);
        ((StatusEffectInstanceAccessor) effect).setAmplifier(amplifier);
        if (effect.getDuration() < 20) ((StatusEffectInstanceAccessor) effect).setDuration(20);
    }

    public enum Mode {
        Normal,
        Haste1,
        Haste2
    }
}
