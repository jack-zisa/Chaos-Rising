package dev.creoii.chaos.inventory;

import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.c2s.SlotUpdateC2S;

import javax.annotation.Nullable;
import java.util.Arrays;

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

    public boolean isEmpty() {
        return Arrays.stream(slots).allMatch(slotRow -> Arrays.stream(slotRow).noneMatch(Slot::hasItem));
    }

    @Nullable
    public Slot addItem(ItemStack stack) {
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
            return firstValid;
        }

        return null;
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

    public void updateSlot(SlotUpdateC2S.Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot) {
        if (action == SlotUpdateC2S.Action.SWAP) {
            from.onRemoveItemFromSlot(fromSlot, fromSlot.getStack());
            to.onRemoveItemFromSlot(toSlot, toSlot.getStack());
            ItemStack takeTouched = toSlot.takeStack();
            toSlot.setStack(fromSlot.getStack().copy());
            from.onAddItemToSlot(toSlot, toSlot.getStack());
            fromSlot.setStack(takeTouched);
            to.onAddItemToSlot(fromSlot, takeTouched);
        } else if (action == SlotUpdateC2S.Action.MOVE) {
            from.onRemoveItemFromSlot(fromSlot, fromSlot.getStack());
            ItemStack moved = fromSlot.takeStack();
            toSlot.setStack(moved);
            to.onAddItemToSlot(toSlot, moved);
        } else if (action == SlotUpdateC2S.Action.QUICK_MOVE) {
            from.onRemoveItemFromSlot(fromSlot, fromSlot.getStack());
            to.addItem(toSlot.takeStack());
        }
    }
}
