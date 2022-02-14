 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.render.WallHack;
import pauln07.pentiumplus.systems.modules.render.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {

    @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
    private static void onGetBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> cir) {
        if(Modules.get() != null) {
            WallHack wallHack = Modules.get().get(WallHack.class);
            Xray xray = Modules.get().get(Xray.class);

            if(wallHack.isActive()) {
                if(wallHack.blocks.get().contains(state.getBlock())) {
                    cir.setReturnValue(RenderLayer.getTranslucent());
                }
            } else if(xray.isActive()) {
                cir.setReturnValue(RenderLayer.getTranslucent());
            }
        }
    }

}
