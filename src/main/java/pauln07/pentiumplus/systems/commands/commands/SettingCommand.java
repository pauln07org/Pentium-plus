 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.systems.commands.Command;
import pauln07.pentiumplus.systems.commands.arguments.ModuleArgumentType;
import pauln07.pentiumplus.systems.commands.arguments.SettingArgumentType;
import pauln07.pentiumplus.systems.commands.arguments.SettingValueArgumentType;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SettingCommand extends Command {
    public SettingCommand() {
        super("settings", "Allows you to view and change module settings.", "s");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", ModuleArgumentType.module())
                .then(
                        argument("setting", SettingArgumentType.setting())
                        .executes(context -> {
                            // Get setting value
                            Setting<?> setting = SettingArgumentType.getSetting(context);

                            ModuleArgumentType.getModule(context, "module").info("Setting (highlight)%s(default) is (highlight)%s(default).", setting.title, setting.get());

                            return SINGLE_SUCCESS;
                        })
                        .then(
                                argument("value", SettingValueArgumentType.value())
                                .executes(context -> {
                                    // Set setting value
                                    Setting<?> setting = SettingArgumentType.getSetting(context);
                                    String value = context.getArgument("value", String.class);

                                    if (setting.parse(value)) {
                                        ModuleArgumentType.getModule(context, "module").info("Setting (highlight)%s(default) changed to (highlight)%s(default).", setting.title, value);
                                    }

                                    return SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
