 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixininterface;

import pauln07.pentiumplus.utils.misc.Vec4;
import net.minecraft.util.math.Vec3d;

public interface IMatrix4f {
    void multiplyMatrix(Vec4 v, Vec4 out);

    Vec3d mul(Vec3d vec);
}
