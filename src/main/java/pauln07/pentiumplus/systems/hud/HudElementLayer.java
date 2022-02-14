 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.hud;

import pauln07.pentiumplus.systems.hud.modules.HudElement;
import pauln07.pentiumplus.utils.render.AlignmentX;
import pauln07.pentiumplus.utils.render.AlignmentY;

import java.util.ArrayList;
import java.util.List;

public class HudElementLayer {
    private final HudRenderer renderer;
    private final List<HudElement> allElements;
    private final List<HudElement> elements;

    private final AlignmentX xAlign;
    private final AlignmentY yAlign;

    private final int xOffset, yOffset;

    public HudElementLayer(HudRenderer renderer, List<HudElement> allElements, AlignmentX xAlign, AlignmentY yAlign, int xOffset, int yOffset) {
        this.renderer = renderer;
        this.allElements = allElements;
        this.elements = new ArrayList<>();
        this.xAlign = xAlign;
        this.yAlign = yAlign;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void add(HudElement element) {
        allElements.add(element);
        elements.add(element);

        element.settings.registerColorSettings(null);
    }

    public void align() {
        double x = xOffset * (xAlign == AlignmentX.Right ? -1 : 1);
        double y = yOffset * (yAlign == AlignmentY.Bottom ? -1 : 1);

        for (HudElement element : elements) {
            element.update(renderer);

            element.box.x = xAlign;
            element.box.y = yAlign;
            element.box.xOffset = (int) Math.round(x);
            element.box.yOffset = (int) Math.round(y);

            if (yAlign == AlignmentY.Bottom) y -= element.box.height;
            else y += element.box.height;
        }
    }
}
