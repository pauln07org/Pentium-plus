 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixininterface;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public interface ILivingEntityRenderer {
    void setupTransformsInterface(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);
    void scaleInterface(LivingEntity entity, MatrixStack matrices, float amount);
    boolean isVisibleInterface(LivingEntity entity);
    float getAnimationCounterInterface(LivingEntity entity, float tickDelta);
}
