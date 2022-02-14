 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.renderer;

public class ShaderMesh extends Mesh {
    private final Shader shader;

    public ShaderMesh(Shader shader, DrawMode drawMode, Attrib... attributes) {
        super(drawMode, attributes);

        this.shader = shader;
    }

    @Override
    protected void beforeRender() {
    	if (shader != null) {
    		shader.bind();
    		
    	}
    }
}
