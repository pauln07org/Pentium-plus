 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.render.Render3DEvent;
import pauln07.pentiumplus.events.render.RenderAfterWorldEvent;
import pauln07.pentiumplus.mixininterface.IVec3d;
import pauln07.pentiumplus.renderer.Renderer3D;
import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.player.LiquidInteract;
import pauln07.pentiumplus.systems.modules.player.NoMiningTrace;
import pauln07.pentiumplus.systems.modules.player.Reach;
import pauln07.pentiumplus.systems.modules.render.Freecam;
import pauln07.pentiumplus.systems.modules.render.NoRender;
import pauln07.pentiumplus.systems.modules.render.Zoom;
import pauln07.pentiumplus.systems.modules.world.HighwayBuilder;
import pauln07.pentiumplus.utils.Utils;
import pauln07.pentiumplus.utils.render.NametagUtils;
import pauln07.pentiumplus.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract void updateTargetedEntity(float tickDelta);

    @Shadow public abstract void reset();

    @Shadow @Final private Camera camera;
    @Unique private Renderer3D renderer;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info, boolean bl, Camera camera, MatrixStack matrixStack, double d, float f, Matrix4f matrix4f) {
        if (!Utils.canUpdate()) return;

        client.getProfiler().push("meteor-client_render");

        if (renderer == null) renderer = new Renderer3D();
        Render3DEvent event = Render3DEvent.get(matrices, renderer, tickDelta, camera.getPos().x, camera.getPos().y, camera.getPos().z);

        RenderUtils.updateScreenCenter();
        NametagUtils.onRender(matrices, matrix4f);

        renderer.begin();
        PentiumPlus.EVENT_BUS.post(event);
        renderer.render(matrices);

        RenderSystem.applyModelViewMatrix();
        client.getProfiler().pop();
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void onRenderWorldTail(CallbackInfo info) {
        PentiumPlus.EVENT_BUS.post(RenderAfterWorldEvent.get());
    }

    @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"), cancellable = true)
    private void onUpdateTargetedEntity(float tickDelta, CallbackInfo info) {
        if (Modules.get().get(NoMiningTrace.class).canWork() && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            client.getProfiler().pop();
            info.cancel();
        }
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"))
    private HitResult updateTargetedEntityEntityRayTraceProxy(Entity entity, double maxDistance, float tickDelta, boolean includeFluids) {
        if (Modules.get().isActive(LiquidInteract.class)) {
            HitResult result = entity.raycast(maxDistance, tickDelta, includeFluids);
            if (result.getType() != HitResult.Type.MISS) return result;

            return entity.raycast(maxDistance, tickDelta, true);
        }
        return entity.raycast(maxDistance, tickDelta, includeFluids);
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onBobViewWhenHurt(MatrixStack matrixStack, float f, CallbackInfo info) {
        if (Modules.get().get(NoRender.class).noHurtCam()) info.cancel();
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && Modules.get().get(NoRender.class).noTotemAnimation()) {
            info.cancel();
        }
    }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float applyCameraTransformationsMathHelperLerpProxy(float delta, float first, float second) {
        if (Modules.get().get(NoRender.class).noNausea()) return 0;
        return MathHelper.lerp(delta, first, second);
    }

    // Freecam

    private boolean freecamSet = false;

    @Inject(method = "updateTargetedEntity", at = @At("HEAD"), cancellable = true)
    private void updateTargetedEntityInvoke(float tickDelta, CallbackInfo info) {
        Freecam freecam = Modules.get().get(Freecam.class);
        boolean highwayBuilder = Modules.get().isActive(HighwayBuilder.class);

        if ((freecam.isActive() || highwayBuilder) && client.getCameraEntity() != null && !freecamSet) {
            info.cancel();
            Entity cameraE = client.getCameraEntity();

            double x = cameraE.getX();
            double y = cameraE.getY();
            double z = cameraE.getZ();
            double prevX = cameraE.prevX;
            double prevY = cameraE.prevY;
            double prevZ = cameraE.prevZ;
            float yaw = cameraE.getYaw();
            float pitch = cameraE.getPitch();
            float prevYaw = cameraE.prevYaw;
            float prevPitch = cameraE.prevPitch;

            if (highwayBuilder) {
                cameraE.setYaw(camera.getYaw());
                cameraE.setPitch(camera.getPitch());
            }
            else {
                ((IVec3d) cameraE.getPos()).set(freecam.pos.x, freecam.pos.y - cameraE.getEyeHeight(cameraE.getPose()), freecam.pos.z);
                cameraE.prevX = freecam.prevPos.x;
                cameraE.prevY = freecam.prevPos.y - cameraE.getEyeHeight(cameraE.getPose());
                cameraE.prevZ = freecam.prevPos.z;
                cameraE.setYaw(freecam.yaw);
                cameraE.setPitch(freecam.pitch);
                cameraE.prevYaw = freecam.prevYaw;
                cameraE.prevPitch = freecam.prevPitch;
            }

            freecamSet = true;
            updateTargetedEntity(tickDelta);
            freecamSet = false;

            ((IVec3d) cameraE.getPos()).set(x, y, z);
            cameraE.prevX = prevX;
            cameraE.prevY = prevY;
            cameraE.prevZ = prevZ;
            cameraE.setYaw(yaw);
            cameraE.setPitch(pitch);
            cameraE.prevYaw = prevYaw;
            cameraE.prevPitch = prevPitch;
        }
    }

    @Inject(method = "renderHand", at = @At("INVOKE"), cancellable = true)
    private void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo info) {
        if (!Modules.get().get(Freecam.class).renderHands() ||
            !Modules.get().get(Zoom.class).renderHands())
            info.cancel();
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3))
    private double updateTargetedEntityModifySurvivalReach(double d) {
        Reach reach = Modules.get().get(Reach.class);
        return reach.isActive() ? reach.getReach() : d;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        Reach reach = Modules.get().get(Reach.class);
        return reach.isActive() ? reach.getReach() * reach.getReach() : d;
    }
}
