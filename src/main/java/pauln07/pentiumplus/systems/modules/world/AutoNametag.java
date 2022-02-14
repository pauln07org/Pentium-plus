 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.world;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.entity.SortPriority;
import pauln07.pentiumplus.utils.entity.TargetUtils;
import pauln07.pentiumplus.utils.player.FindItemResult;
import pauln07.pentiumplus.utils.player.InvUtils;
import pauln07.pentiumplus.utils.player.PlayerUtils;
import pauln07.pentiumplus.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoNametag extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
            .name("entities")
            .description("Which entities to nametag.")
            .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
            .name("range")
            .description("The maximum range an entity can be to be nametagged.")
            .defaultValue(5)
            .min(0)
            .sliderMax(6)
            .build()
    );

    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>()
            .name("priority")
            .description("Priority sort")
            .defaultValue(SortPriority.LowestDistance)
            .build()
    );

    private final Setting<Boolean> renametag = sgGeneral.add(new BoolSetting.Builder()
            .name("renametag")
            .description("Allows already nametagged entities to be renamed.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("Automatically faces towards the mob being nametagged.")
            .defaultValue(true)
            .build()
    );


    private Entity target;
    private boolean offHand;

    public AutoNametag() {
        super(Categories.World, "auto-nametag", "Automatically uses nametags on entities without a nametag. WILL nametag ALL entities in the specified distance.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        // Nametag in hobar
        FindItemResult findNametag = InvUtils.findInHotbar(Items.NAME_TAG);

        if (!findNametag.found()) {
            error("No Nametag in Hotbar");
            toggle();
            return;
        }


        // Target
        target = TargetUtils.get(entity -> {
            if (PlayerUtils.distanceTo(entity) > range.get()) return false;
            if (!entities.get().getBoolean(entity.getType())) return false;
            if (entity.hasCustomName()) {
                return renametag.get() && entity.getCustomName() != mc.player.getInventory().getStack(findNametag.slot()).getName();
            }
            return false;
        }, priority.get());

        if (target == null) return;


        // Swapping slots
        InvUtils.swap(findNametag.slot(), true);

        offHand = findNametag.isOffhand();


        // Interaction
        if (rotate.get()) Rotations.rotate(Rotations.getYaw(target), Rotations.getPitch(target), -100, this::interact);
        else interact();
    }

    private void interact() {
        mc.interactionManager.interactEntity(mc.player, target, offHand ? Hand.OFF_HAND : Hand.MAIN_HAND);
        InvUtils.swapBack();
    }
}
