 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.render;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.ParticleTypeListSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

import java.util.List;

public class Trail extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<ParticleType<?>>> particles = sgGeneral.add(new ParticleTypeListSetting.Builder()
        .name("particles")
        .description("Particles to draw.")
        .defaultValue(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, ParticleTypes.CAMPFIRE_COSY_SMOKE)
        .build()
    );

    private final Setting<Boolean> pause = sgGeneral.add(new BoolSetting.Builder()
        .name("pause-when-stationary")
        .description("Whether or not to add particles when you are not moving.")
        .defaultValue(true)
        .build()
    );


    public Trail() {
        super(Categories.Render, "trail", "Renders a customizable trail behind your player.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (pause.get()
            && mc.player.getVelocity().x == 0
            && mc.player.getVelocity().y == 0
            && mc.player.getVelocity().z == 0) return;

        for (ParticleType<?> particleType : particles.get()) {
            mc.world.addParticle((ParticleEffect) particleType, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
        }
    }
}
