 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.world.AmbientOcclusionEvent;
import pauln07.pentiumplus.events.world.CollisionShapeEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        AmbientOcclusionEvent event = PentiumPlus.EVENT_BUS.post(AmbientOcclusionEvent.get());

        if (event.lightLevel != -1) info.setReturnValue(event.lightLevel);
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (!(state.getFluidState().isEmpty())) {
            CollisionShapeEvent event = PentiumPlus.EVENT_BUS.post(CollisionShapeEvent.get(state.getFluidState().getBlockState(), pos, CollisionShapeEvent.CollisionType.FLUID));

            if (event.shape != null) info.setReturnValue(event.shape);
        } else {
            CollisionShapeEvent event = PentiumPlus.EVENT_BUS.post(CollisionShapeEvent.get(state, pos, CollisionShapeEvent.CollisionType.BLOCK));

            if (event.shape != null) info.setReturnValue(event.shape);
        }
    }
}