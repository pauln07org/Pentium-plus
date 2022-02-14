 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.game;

public class WindowResizedEvent {
    private static final WindowResizedEvent INSTANCE = new WindowResizedEvent();

    public static WindowResizedEvent get() {
        return INSTANCE;
    }
}
