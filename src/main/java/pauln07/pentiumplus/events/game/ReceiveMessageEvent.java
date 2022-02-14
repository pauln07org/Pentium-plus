 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.game;

import pauln07.pentiumplus.events.Cancellable;
import net.minecraft.text.Text;

public class ReceiveMessageEvent extends Cancellable {
    private static final ReceiveMessageEvent INSTANCE = new ReceiveMessageEvent();

    private Text message;
    private boolean modified;
    public int id;

    public static ReceiveMessageEvent get(Text message, int id) {
        INSTANCE.setCancelled(false);
        INSTANCE.message = message;
        INSTANCE.modified = false;
        INSTANCE.id = id;
        return INSTANCE;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text message) {
        this.message = message;
        this.modified = true;
    }

    public boolean isModified() {
        return modified;
    }
}
