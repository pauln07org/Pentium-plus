 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import pauln07.pentiumplus.systems.waypoints.Waypoint;
import pauln07.pentiumplus.systems.waypoints.Waypoints;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WaypointArgumentType implements ArgumentType<String> {
    private static final DynamicCommandExceptionType NO_SUCH_WAYPOINT = new DynamicCommandExceptionType(name -> new LiteralText("Waypoint with name '" + name + "' doesn't exist."));

    public static WaypointArgumentType waypoint() {return new WaypointArgumentType();}

    public static Waypoint getWaypoint(final CommandContext<?> context, final String name) {
        return Waypoints.get().get(context.getArgument(name, String.class));
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());          //ty sb :pray:

        if (Waypoints.get().get(argument) == null) throw NO_SUCH_WAYPOINT.create(argument);
        return argument;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(getExamples(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        List<String> names = new ArrayList<>();
        for (Waypoint waypoint : Waypoints.get()) names.add(waypoint.name);
        return names;
    }
}

