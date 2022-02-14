 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.misc;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class Spam extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("messages")
        .description("Messages to use for spam.")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
        .name("randomise")
        .description("Selects a random message from your spam message list.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
        .name("bypass")
        .description("Add random text at the end of the message to try to bypass anti spams.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> length = sgGeneral.add(new IntSetting.Builder()
        .name("length")
        .description("Number of characters used to bypass anti spam.")
        .visible(bypass::get)
        .defaultValue(16)
        .sliderRange(1, 256)
        .build()
    );

    private int messageI, timer;

    public Spam() {
        super(Categories.Misc, "spam", "Spams specified messages in chat.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
        messageI = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (messages.get().isEmpty()) return;

        if (timer <= 0) {
            int i;
            if (random.get()) {
                i = Utils.random(0, messages.get().size());
            }
            else {
                if (messageI >= messages.get().size()) messageI = 0;
                i = messageI++;
            }

            String text = messages.get().get(i);
            if (bypass.get()) {
                text += " " + RandomStringUtils.randomAlphabetic(length.get()).toLowerCase();
            }

            mc.player.sendChatMessage(text);
            timer = delay.get();
        }
        else {
            timer--;
        }
    }
}