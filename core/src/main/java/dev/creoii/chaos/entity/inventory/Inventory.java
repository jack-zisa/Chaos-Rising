package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

import java.util.Arrays;

public class Inventory {
    protected final Slot[][] slots;

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

    public boolean isEmpty() {
        return Arrays.stream(slots).allMatch(slotRow -> Arrays.stream(slotRow).noneMatch(Slot::hasItem));
    }

    public boolean addItem(ItemStack stack) {
        Slot firstValid = null;
        for (int i = slots.length - 1; i >= 0; --i) {
            for (Slot slot : slots[i]) {
                if (!slot.hasItem() && slot.canAccept(stack.getItem())) {
                    firstValid = slot;
                    break;
                }
            }
            if (firstValid != null)
                break;
        }

        if (firstValid != null) {
            firstValid.setStack(stack);
            onAddItemToSlot(firstValid, stack);
            return true;
        }

        return false;
    }

    /**
     * Assumes that the item is already added to the slot
     */
    public void onAddItemToSlot(Slot slot, ItemStack stack) {
    }

    /**
     * Assumes that the item is still in the slot
     */
    public void onRemoveItemFromSlot(Slot slot, ItemStack stack) {
    }
}
