 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.LlamaEntity;

import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.movement.EntityControl;


@Mixin(LlamaEntity.class)
public class LlamaEntityMixin {
    @Inject(method = "canBeControlledByRider", at = @At("HEAD"), cancellable = true)
    public void canBeControlledByRider(CallbackInfoReturnable<Boolean> ci) {
        if (Modules.get().get(EntityControl.class).isActive()) ci.setReturnValue(true);
    }
}
