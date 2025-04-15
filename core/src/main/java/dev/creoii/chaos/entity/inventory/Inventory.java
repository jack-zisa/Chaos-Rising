package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

public class Inventory {
    private final Slot[][] slots;

    public Inventory(int rows, int cols) {
        slots = new Slot[rows][cols];
        for (int r = 0; r < slots.length; ++r) {
            for (int c = 0; c < slots[r].length; ++c) {
                slots[r][c] = new Slot();
            }
        }
    }

    public Slot[][] getSlots() {
        return slots;
    }

    public Slot getSlot(int ri, int ci) {
        return slots[ri][ci];
    }

    public boolean addItem(ItemStack stack) {
        Slot firstEmpty = null;
        for (Slot[] slotRow : slots) {
            for (Slot slot : slotRow) {
                if (firstEmpty == null && !slot.hasItem()) {
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
