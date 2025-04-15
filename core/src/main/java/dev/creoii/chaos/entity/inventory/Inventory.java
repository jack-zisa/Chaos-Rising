package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

public class Inventory {
    private final Slot[][] slots;

    public Inventory(int rows, int cols) {
        slots = new Slot[rows][cols];
        for (int r = 0; r < slots.length; ++r) {
            for (int c = 0; c < slots[r].length; ++c) {
                slots[r][c] = new Slot(r, c);
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
        for (int i = slots.length - 1; i >= 0; --i) {
            for (Slot slot : slots[i]) {
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

    public void swap(int x1, int y1, int x2, int y2) {
        Slot temp = getSlot(x1, y1).copy();
        Slot other = getSlot(x2, y2);
        getSlot(x1, y1).setStack(other.getStack());
        other.setStack(temp.getStack());
    }
}
