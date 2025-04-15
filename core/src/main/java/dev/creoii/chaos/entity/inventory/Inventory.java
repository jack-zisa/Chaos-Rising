package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

import java.util.Arrays;

public class Inventory {
    private final Slot[][] inventory;

    public Inventory(int rows, int cols) {
        inventory = new Slot[rows][cols];
        for (Slot[] slotRow : inventory) {
            Arrays.fill(slotRow, new Slot());
        }
    }

    public Slot[][] getInventory() {
        return inventory;
    }

    public boolean addItem(ItemStack stack) {
        Slot firstEmpty = null;
        for (Slot[] slotRow : inventory) {
            for (Slot slot : slotRow) {
                if (slot != null && slot.getStack().canStackWith(stack)) {
                    slot.getStack().add(stack);
                    return true;
                } else if (firstEmpty == null) {
                    firstEmpty = slot;
                }
            }
        }

        if (firstEmpty != null) {
            firstEmpty.setStack(stack);
            return true;
        }

        return false;
    }
}
