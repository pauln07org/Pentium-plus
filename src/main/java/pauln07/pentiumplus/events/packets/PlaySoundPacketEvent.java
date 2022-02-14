 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.events.packets;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

public class PlaySoundPacketEvent {

    private static final PlaySoundPacketEvent INSTANCE = new PlaySoundPacketEvent();

    public PlaySoundS2CPacket packet;

    public static PlaySoundPacketEvent get(PlaySoundS2CPacket packet) {
        INSTANCE.packet = packet;
        return INSTANCE;
    }
}
