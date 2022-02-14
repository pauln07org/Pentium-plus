 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixininterface;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public interface IEntityRenderer {
    Identifier getTextureInterface(Entity entity);
}
