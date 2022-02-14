 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.packets;

import pauln07.pentiumplus.events.Cancellable;
import net.minecraft.network.Packet;

public class PacketEvent extends Cancellable {
    public Packet<?> packet;

    public static class Receive extends PacketEvent {
        private static final Receive INSTANCE = new Receive();

        public static Receive get(Packet<?> packet) {
            INSTANCE.setCancelled(false);
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Send extends PacketEvent {
        private static final Send INSTANCE = new Send();

        public static Send get(Packet<?> packet) {
            INSTANCE.setCancelled(false);
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }

    public static class Sent extends PacketEvent {
        private static final Sent INSTANCE = new Sent();

        public static Sent get(Packet<?> packet) {
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }
}
