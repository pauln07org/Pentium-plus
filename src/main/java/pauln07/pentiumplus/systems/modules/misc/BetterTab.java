 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.misc;

import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.systems.friends.Friend;
import pauln07.pentiumplus.systems.friends.Friends;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.render.color.Color;
import pauln07.pentiumplus.utils.render.color.SettingColor;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class BetterTab extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Integer> tabSize = sgGeneral.add(new IntSetting.Builder()
            .name("tablist-size")
            .description("Bypasses the 80 player limit on the tablist.")
            .defaultValue(100)
            .min(1)
            .sliderRange(1, 1000)
            .build()
    );

    private final Setting<Boolean> self = sgGeneral.add(new BoolSetting.Builder()
            .name("highlight-self")
            .description("Highlights yourself in the tablist.")
            .defaultValue(true)
            .build()
    );

    private final Setting<SettingColor> selfColor = sgGeneral.add(new ColorSetting.Builder()
            .name("self-color")
            .description("The color to highlight your name with.")
            .defaultValue(new SettingColor(250, 130, 30))
            .visible(self::get)
            .build()
    );

    private final Setting<Boolean> friends = sgGeneral.add(new BoolSetting.Builder()
            .name("highlight-friends")
            .description("Highlights friends in the tablist.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> accurateLatency = sgGeneral.add(new BoolSetting.Builder()
        .name("accurate-latency")
        .description("Shows latency as a number in the tablist.")
        .defaultValue(true)
        .build()
    );


    public BetterTab() {
        super(Categories.Misc, "better-tab", "Various improvements to the tab list.");
    }

    public Text getPlayerName(PlayerListEntry playerListEntry) {
        Text name;
        Color color = null;

        name = playerListEntry.getDisplayName();
        if (name == null) name = new LiteralText(playerListEntry.getProfile().getName());

        if (playerListEntry.getProfile().getId().toString().equals(mc.player.getGameProfile().getId().toString()) && self.get()) {
            color = selfColor.get();
        }
        else if (friends.get() && Friends.get().get(playerListEntry.getProfile().getName()) != null) {
            Friend friend = Friends.get().get(playerListEntry.getProfile().getName());
            if (friend != null) color = Friends.get().color;
        }

        if (color != null) {
            String nameString = name.getString();

            for (Formatting format : Formatting.values()) {
                if (format.isColor()) nameString = nameString.replace(format.toString(), "");
            }

            name = new LiteralText(nameString).setStyle(name.getStyle().withColor(new TextColor(color.getPacked())));
        }

        return name;
    }

}
