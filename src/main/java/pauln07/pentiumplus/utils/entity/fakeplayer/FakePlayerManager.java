 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.utils.entity.fakeplayer;

import java.util.ArrayList;
import java.util.List;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class FakePlayerManager {
    private static final List<FakePlayerEntity> fakePlayers = new ArrayList<>();

    public static void add(String name, float health, boolean copyInv) {
        FakePlayerEntity fakePlayer = new FakePlayerEntity(mc.player, name, health, copyInv);
        fakePlayer.spawn();

        fakePlayers.add(fakePlayer);
    }

    public static void clear() {
        if (fakePlayers.isEmpty()) return;
        fakePlayers.forEach(FakePlayerEntity::despawn);
        fakePlayers.clear();
    }

    public static List<FakePlayerEntity> getPlayers() {
        return fakePlayers;
    }

    public static int size() {
        return fakePlayers.size();
    }
}
