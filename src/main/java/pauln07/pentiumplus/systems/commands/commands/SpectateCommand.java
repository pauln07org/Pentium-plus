 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.meteor.KeyEvent;
import pauln07.pentiumplus.systems.commands.Command;
import pauln07.pentiumplus.systems.commands.arguments.PlayerArgumentType;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SpectateCommand extends Command {

    private final StaticListener shiftListener = new StaticListener();

    public SpectateCommand() {
        super("spectate", "Allows you to spectate nearby players");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("reset").executes(context -> {
            mc.setCameraEntity(mc.player);
            return SINGLE_SUCCESS;
        }));

        builder.then(argument("player", PlayerArgumentType.player()).executes(context -> {
            mc.setCameraEntity(PlayerArgumentType.getPlayer(context));
            mc.player.sendMessage(new LiteralText("Sneak to un-spectate."), true);
            PentiumPlus.EVENT_BUS.subscribe(shiftListener);
            return SINGLE_SUCCESS;
        }));
    }

    private class StaticListener {
        @EventHandler
        private void onKey(KeyEvent event) {
            if (mc.options.keySneak.matchesKey(event.key, 0) || mc.options.keySneak.matchesMouse(event.key)) {
                mc.setCameraEntity(mc.player);
                event.cancel();
                PentiumPlus.EVENT_BUS.unsubscribe(this);
            }
        }
    }
}
