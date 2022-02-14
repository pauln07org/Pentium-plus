 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("currentFps")
    static int getFps() {
        return 0;
    }

    @Mutable
    @Accessor("session")
    void setSession(Session session);

    @Accessor("networkProxy")
    Proxy getProxy();

    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int itemUseCooldown);

    @Accessor("itemUseCooldown")
    int getItemUseCooldown();

    @Invoker("doAttack")
    void leftClick();
}
