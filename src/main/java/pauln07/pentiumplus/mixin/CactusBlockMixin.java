 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.world.AntiCactus;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Inject(method = "getCollisionShape", at = {@At("HEAD")}, cancellable = true)
    private void onGetCollisionShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, ShapeContext entityContext_1, CallbackInfoReturnable<VoxelShape> infoR)
    {
        if (Modules.get().isActive(AntiCactus.class)) {
            infoR.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
