 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class HeldItemRendererEvent {
    private static final HeldItemRendererEvent INSTANCE = new HeldItemRendererEvent();

    public Hand hand;
    public MatrixStack matrix;

    public static HeldItemRendererEvent get(Hand hand, MatrixStack matrices) {
        INSTANCE.hand = hand;
        INSTANCE.matrix = matrices;
        return INSTANCE;
    }
}
