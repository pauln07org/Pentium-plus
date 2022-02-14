 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.player;

import pauln07.pentiumplus.settings.BoolSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import net.minecraft.item.PickaxeItem;

public class NoMiningTrace extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> onlyWhenHoldingPickaxe = sgGeneral.add(new BoolSetting.Builder()
            .name("only-when-holding-a-pickaxe")
            .description("Whether or not to work only when holding a pickaxe.")
            .defaultValue(true)
            .build()
    );

    public NoMiningTrace() {
        super(Categories.Player, "no-mining-trace", "Allows you to mine blocks through entities.");
    }

    public boolean canWork() {
        if (!isActive()) return false;

        if (onlyWhenHoldingPickaxe.get()) {
            return mc.player.getMainHandStack().getItem() instanceof PickaxeItem || mc.player.getOffHandStack().getItem() instanceof PickaxeItem;
        }

        return true;
    }
}
