 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.addons;

import pauln07.pentiumplus.utils.Init;
import pauln07.pentiumplus.utils.InitStage;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {
    public static MeteorAddon METEOR;
    public static final List<MeteorAddon> ADDONS = new ArrayList<>();

    @Init(stage = InitStage.Pre)
    public static void init() {
        // Meteor pseudo addon
        {
            METEOR = new MeteorAddon() {
                @Override
                public void onInitialize() {}
            };

            ModMetadata metadata = FabricLoader.getInstance().getModContainer("meteor-client").get().getMetadata();

            METEOR.name = metadata.getName();
            METEOR.authors = new String[metadata.getAuthors().size()];
            if (metadata.containsCustomValue("meteor-client:color")) METEOR.color.parse(metadata.getCustomValue("meteor-client:color").getAsString());

            int i = 0;
            for (Person author : metadata.getAuthors()) {
                METEOR.authors[i++] = author.getName();
            }
        }

        // Addons
        for (EntrypointContainer<MeteorAddon> entrypoint : FabricLoader.getInstance().getEntrypointContainers("meteor", MeteorAddon.class)) {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            MeteorAddon addon = entrypoint.getEntrypoint();

            addon.name = metadata.getName();
            addon.authors = new String[metadata.getAuthors().size()];
            if (metadata.containsCustomValue("meteor-client:color")) addon.color.parse(metadata.getCustomValue("meteor-client:color").getAsString());

            int i = 0;
            for (Person author : metadata.getAuthors()) {
                addon.authors[i++] = author.getName();
            }

            ADDONS.add(addon);
        }
    }
}
