 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.misc;

import io.netty.buffer.Unpooled;
import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.packets.PacketEvent;
import pauln07.pentiumplus.mixin.CustomPayloadC2SPacketAccessor;
import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.settings.StringSetting;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class ServerSpoof extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> brand = sgGeneral.add(new StringSetting.Builder()
            .name("brand")
            .description("Specify the brand that will be send to the server.")
            .defaultValue("vanilla")
            .build()
    );

    private final Setting<Boolean> resourcePack = sgGeneral.add(new BoolSetting.Builder()
            .name("resource-pack")
            .description("Spoof accepting server resource pack.")
            .defaultValue(false)
            .build()
    );

    public ServerSpoof() {
        super(Categories.Misc, "server-spoof", "Spoof client brand and/or resource pack.");

        PentiumPlus.EVENT_BUS.subscribe(new Listener());
    }

    private class Listener {
        @EventHandler
        private void onPacketSend(PacketEvent.Send event) {
            if (!isActive()) return;
            if (!(event.packet instanceof CustomPayloadC2SPacket)) return;
            CustomPayloadC2SPacketAccessor packet = (CustomPayloadC2SPacketAccessor) event.packet;
            Identifier id = packet.getChannel();

            if (id.equals(CustomPayloadC2SPacket.BRAND)) {
                packet.setData(new PacketByteBuf(Unpooled.buffer()).writeString(brand.get()));
            }
            else if (StringUtils.containsIgnoreCase(packet.getData().toString(StandardCharsets.UTF_8), "fabric") && brand.get() != "fabric") {
                event.cancel();
            }
        }

        @EventHandler
        private void onPacketRecieve(PacketEvent.Receive event) {
            if (!isActive() || !resourcePack.get()) return;
            if (!(event.packet instanceof ResourcePackSendS2CPacket)) return;
            event.cancel();
            ResourcePackSendS2CPacket packet = (ResourcePackSendS2CPacket) event.packet;
            BaseText msg = new LiteralText("This server has ");
            msg.append(packet.isRequired() ? "a required " : "an optional ");
            BaseText link = new LiteralText("resource pack");
            link.setStyle(link.getStyle()
                .withColor(Formatting.BLUE)
                .withUnderline(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, packet.getURL()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to download")))
            );
            msg.append(link);
            msg.append(".");
            info(msg);
        }
    }
}
