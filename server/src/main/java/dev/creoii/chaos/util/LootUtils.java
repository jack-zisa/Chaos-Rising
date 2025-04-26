package dev.creoii.chaos.util;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.loot.LootTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class LootUtils {
    private static final Random RANDOM = new Random();

    public static void fillInventory(ServerGame game, Inventory inventory, LootTable lootTable, int rolls) {
        List<ItemStack> loot = lootTable.roll(game, rolls);
        List<Slot> availableSlots = new ArrayList<>();

        for (Slot[] slotRow : inventory.getSlots()) {
            for (Slot slot : slotRow) {
                if (!slot.hasItem())
                    availableSlots.add(slot);
            }
        }

        if (availableSlots.isEmpty())
            return;

        Collections.shuffle(availableSlots, RANDOM);

        for (ItemStack stack : loot) {
            Slot slot = availableSlots.removeFirst();
            slot.setStack(stack);
        }
    }
}
