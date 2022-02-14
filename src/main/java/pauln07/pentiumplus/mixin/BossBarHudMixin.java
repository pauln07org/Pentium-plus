 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.render.RenderBossBarEvent;
import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.render.NoRender;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Iterator;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo info) {
        if (Modules.get().get(NoRender.class).noBossBar()) info.cancel();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"))
    public Iterator<ClientBossBar> onRender(Collection<ClientBossBar> collection) {
        RenderBossBarEvent.BossIterator event = PentiumPlus.EVENT_BUS.post(RenderBossBarEvent.BossIterator.get(collection.iterator()));
        return event.iterator;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ClientBossBar;getName()Lnet/minecraft/text/Text;"))
    public Text onAsFormattedString(ClientBossBar clientBossBar) {
        RenderBossBarEvent.BossText event = PentiumPlus.EVENT_BUS.post(RenderBossBarEvent.BossText.get(clientBossBar, clientBossBar.getName()));
        return event.name;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 9, ordinal = 1))
    public int modifySpacingConstant(int j) {
        RenderBossBarEvent.BossSpacing event = PentiumPlus.EVENT_BUS.post(RenderBossBarEvent.BossSpacing.get(j));
        return event.spacing;
    }
}
