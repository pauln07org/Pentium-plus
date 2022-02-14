 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import net.minecraft.client.render.BufferRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferRenderer.class)
public interface BufferRendererAccessor {
    @Accessor("currentVertexArray")
    static void setCurrentVertexArray(int vao) {}

    @Accessor("currentVertexBuffer")
    static void setCurrentVertexBuffer(int vbo) {}

    @Accessor("currentElementBuffer")
    static void setCurrentElementBuffer(int ibo) {}
}
