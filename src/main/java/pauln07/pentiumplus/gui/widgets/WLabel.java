 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.widgets;

import pauln07.pentiumplus.utils.render.color.Color;

public abstract class WLabel extends WWidget {
    public Color color;

    protected String text;
    protected boolean title;

    public WLabel(String text, boolean title) {
        this.text = text;
        this.title = title;
    }

    @Override
    protected void onCalculateSize() {
        width = theme.textWidth(text, text.length(), title);
        height = theme.textHeight(title);
    }

    public void set(String text) {
        if (Math.round(theme.textWidth(text, text.length(), title)) != width) invalidate();

        this.text = text;
    }

    public String get() {
        return text;
    }
}
