package dev.creoii.chaos.util;

import dev.creoii.chaos.World;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.loot.LootTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LootUtils {
    public static void fillInventory(World world, Inventory inventory, LootTable lootTable, int rolls) {
        List<ItemStack> loot = lootTable.roll(world, rolls);
        List<Slot> availableSlots = new ArrayList<>();

        for (Slot[] slotRow : inventory.getSlots()) {
            for (Slot slot : slotRow) {
                if (!slot.hasItem())
                    availableSlots.add(slot);
            }
        }

        if (availableSlots.isEmpty())
            return;

        Collections.shuffle(availableSlots, world.getRandom());

        for (ItemStack stack : loot) {
            Slot slot = availableSlots.removeFirst();
            slot.setStack(stack);
        }
    }
}
