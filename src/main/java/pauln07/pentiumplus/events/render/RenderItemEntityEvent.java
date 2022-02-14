 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.render;

import pauln07.pentiumplus.events.Cancellable;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;

import java.util.Random;

public class RenderItemEntityEvent extends Cancellable {
    private static final RenderItemEntityEvent INSTANCE = new RenderItemEntityEvent();

    public ItemEntity itemEntity;
    public float f;
    public float tickDelta;
    public MatrixStack matrixStack;
    public VertexConsumerProvider vertexConsumerProvider;
    public int light;
    public Random random;
    public ItemRenderer itemRenderer;

    public static RenderItemEntityEvent get(ItemEntity itemEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, Random random, ItemRenderer itemRenderer) {
        INSTANCE.setCancelled(false);
        INSTANCE.itemEntity = itemEntity;
        INSTANCE.f = f;
        INSTANCE.tickDelta = tickDelta;
        INSTANCE.matrixStack = matrixStack;
        INSTANCE.vertexConsumerProvider = vertexConsumerProvider;
        INSTANCE.light = light;
        INSTANCE.random = random;
        INSTANCE.itemRenderer = itemRenderer;
        return INSTANCE;
    }
}
