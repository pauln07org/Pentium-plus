 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.utils.render;

import pauln07.pentiumplus.mixin.WorldRendererAccessor;
import pauln07.pentiumplus.renderer.GL;
import pauln07.pentiumplus.renderer.PostProcessRenderer;
import pauln07.pentiumplus.renderer.Shader;
import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.render.Chams;
import pauln07.pentiumplus.systems.modules.render.ESP;
import pauln07.pentiumplus.utils.Init;
import pauln07.pentiumplus.utils.InitStage;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class EntityShaders {
    // Overlay
    public static Framebuffer overlayFramebuffer;
    public static OutlineVertexConsumerProvider overlayVertexConsumerProvider;
    private static Shader overlayShader;

    private static Chams chams;
    public static float timer;

    // Outline
    public static Framebuffer outlinesFramebuffer;
    public static OutlineVertexConsumerProvider outlinesVertexConsumerProvider;
    private static Shader outlinesShader;

    private static ESP esp;
    public static boolean renderingOutlines;

    // Overlay

    public static void initOverlay(String shaderName) {
        overlayShader = new Shader("outline.vert", shaderName + ".frag");
        overlayFramebuffer = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
        overlayVertexConsumerProvider = new OutlineVertexConsumerProvider(mc.getBufferBuilders().getEntityVertexConsumers());
        timer = 0;
    }

    public static boolean shouldDrawOverlay(Entity entity) {
        if (chams == null) chams = Modules.get().get(Chams.class);
        return chams.isShader() && chams.entities.get().getBoolean(entity.getType()) && (entity != mc.player || !chams.ignoreSelfDepth.get());
    }

    // Outlines

    @Init(stage = InitStage.Pre)
    public static void initOutlines() {
        outlinesShader = new Shader("outline.vert", "outline.frag");
        outlinesFramebuffer = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
        outlinesVertexConsumerProvider = new OutlineVertexConsumerProvider(mc.getBufferBuilders().getEntityVertexConsumers());
    }

    public static boolean shouldDrawOutline(Entity entity) {
        if (esp == null) esp = Modules.get().get(ESP.class);
        return esp.isShader() && esp.getOutlineColor(entity) != null && (entity != mc.player || !esp.ignoreSelf.get());
    }

    // Main

    public static void beginRender() {
        // Overlay
        if (chams == null) chams = Modules.get().get(Chams.class);
        if (chams.isShader()) overlayFramebuffer.clear(false);

        // Outline
        if (esp == null) esp = Modules.get().get(ESP.class);
        if (esp.isShader()) outlinesFramebuffer.clear(false);

        mc.getFramebuffer().beginWrite(false);
    }

    public static void endRender() {
        WorldRenderer worldRenderer = mc.worldRenderer;
        WorldRendererAccessor wra = (WorldRendererAccessor) worldRenderer;
        Framebuffer fbo = worldRenderer.getEntityOutlinesFramebuffer();

        // Overlay
        if (chams != null && chams.isShader()) {
            wra.setEntityOutlinesFramebuffer(overlayFramebuffer);
            overlayVertexConsumerProvider.draw();
            wra.setEntityOutlinesFramebuffer(fbo);

            mc.getFramebuffer().beginWrite(false);

            GL.bindTexture(overlayFramebuffer.getColorAttachment());

            overlayShader.bind();
            overlayShader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            overlayShader.set("u_Texture", 0);
            overlayShader.set("u_Time", timer++ / 20.0);
            PostProcessRenderer.render();
        }

        // Outline
        if (esp != null && esp.isShader()) {
            wra.setEntityOutlinesFramebuffer(outlinesFramebuffer);
            outlinesVertexConsumerProvider.draw();
            wra.setEntityOutlinesFramebuffer(fbo);

            mc.getFramebuffer().beginWrite(false);

            GL.bindTexture(outlinesFramebuffer.getColorAttachment());

            outlinesShader.bind();
            outlinesShader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            outlinesShader.set("u_Texture", 0);
            outlinesShader.set("u_Width", esp.outlineWidth.get());
            outlinesShader.set("u_FillOpacity", esp.fillOpacity.get().floatValue() / 255.0);
            outlinesShader.set("u_ShapeMode", esp.shapeMode.get().ordinal());
            PostProcessRenderer.render();
        }
    }

    public static void onResized(int width, int height) {
        if (overlayFramebuffer != null) overlayFramebuffer.resize(width, height, false);
        if (outlinesFramebuffer != null) outlinesFramebuffer.resize(width, height, false);
    }
}
