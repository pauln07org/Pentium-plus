 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import pauln07.pentiumplus.systems.commands.Command;
import pauln07.pentiumplus.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;
import meteordevelopment.starscript.utils.StarscriptError;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SayCommand extends Command {
    public SayCommand() {
        super("say", "Sends messages in chat.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
            String msg = context.getArgument("message", String.class);
            Parser.Result result = Parser.parse(msg);

            if (result.hasErrors()) {
                for (Error error : result.errors) MeteorStarscript.printChatError(error);
            }
            else {
                Script script = Compiler.compile(result);

                try {
                    String message = MeteorStarscript.ss.run(script);
                    mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(message));
                } catch (StarscriptError e) {
                    MeteorStarscript.printChatError(e);
                }
            }

            return SINGLE_SUCCESS;
        }));
    }
}
