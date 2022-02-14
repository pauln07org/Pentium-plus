 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.movement.ElytraBoost;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {
    @Shadow protected abstract void explodeAndRemove();

    @Shadow private int life;

    @Shadow private int lifeTime;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo info) {
        if (Modules.get().get(ElytraBoost.class).isFirework((FireworkRocketEntity) (Object) this) && this.life > this.lifeTime) {
            this.explodeAndRemove();
        }
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo info) {
        if (Modules.get().get(ElytraBoost.class).isFirework((FireworkRocketEntity) (Object) this)) {
            this.explodeAndRemove();
            info.cancel();
        }
    }

    @Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
    private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo info) {
        if (Modules.get().get(ElytraBoost.class).isFirework((FireworkRocketEntity) (Object) this)) {
            this.explodeAndRemove();
            info.cancel();
        }
    }
}
