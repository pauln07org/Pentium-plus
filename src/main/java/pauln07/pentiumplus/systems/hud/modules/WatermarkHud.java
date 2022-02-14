 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.hud.modules;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.systems.hud.HUD;

public class WatermarkHud extends DoubleTextHudElement {
    public WatermarkHud(HUD hud) {
        super(hud, "watermark", "Displays a Pentium plus watermark.", "Pentium plus ");
    }

    @Override
    protected String getRight() {
        if (PentiumPlus.DEV_BUILD.isEmpty()) {
            return PentiumPlus.VERSION.toString();
        }

        return PentiumPlus.VERSION + " " + PentiumPlus.DEV_BUILD;
    }
}
