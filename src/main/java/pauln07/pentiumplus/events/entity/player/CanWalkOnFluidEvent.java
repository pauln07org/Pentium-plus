 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.entity.player;

import net.minecraft.fluid.Fluid;

public class CanWalkOnFluidEvent {
    private static final CanWalkOnFluidEvent INSTANCE = new CanWalkOnFluidEvent();

    public Fluid fluid;
    public boolean walkOnFluid;

    public static CanWalkOnFluidEvent get(Fluid fluid) {
        INSTANCE.fluid = fluid;
        INSTANCE.walkOnFluid = false;
        return INSTANCE;
    }
}
