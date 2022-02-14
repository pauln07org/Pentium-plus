 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.utils.player;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.events.game.OpenScreenEvent;
import pauln07.pentiumplus.events.world.BlockActivateEvent;
import pauln07.pentiumplus.utils.Init;
import pauln07.pentiumplus.utils.InitStage;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.collection.DefaultedList;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class EChestMemory {
    public static final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private static int echestOpenedState;

    @Init(stage = InitStage.Pre)
    public static void init() {
        PentiumPlus.EVENT_BUS.subscribe(EChestMemory.class);
    }

    @EventHandler
    private static void onBlockActivate(BlockActivateEvent event) {
        if (event.blockState.getBlock() instanceof EnderChestBlock && echestOpenedState == 0) echestOpenedState = 1;
    }

    @EventHandler
    private static void onOpenScreenEvent(OpenScreenEvent event) {
        if (echestOpenedState == 1 && event.screen instanceof GenericContainerScreen) {
            echestOpenedState = 2;
            return;
        }
        if (echestOpenedState == 0) return;

        if (!(mc.currentScreen instanceof GenericContainerScreen)) return;
        GenericContainerScreenHandler container = ((GenericContainerScreen) mc.currentScreen).getScreenHandler();
        if (container == null) return;
        Inventory inv = container.getInventory();

        for (int i = 0; i < 27; i++) {
            ITEMS.set(i, inv.getStack(i));
        }

        echestOpenedState = 0;
    }
}
