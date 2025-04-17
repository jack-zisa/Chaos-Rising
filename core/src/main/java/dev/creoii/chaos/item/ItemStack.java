package dev.creoii.chaos.item;

import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.entity.inventory.Slot;

import javax.annotation.Nullable;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(null, 0);
    @Nullable
    private Item item;
    private int count;

    public ItemStack(@Nullable Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    @Nullable
    public Item getItem() {
        return item;
    }

    public void setItem(@Nullable Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemStack copy() {
        return new ItemStack(item, count);
    }

    /**
     * @return false to allow dragging, true to disable dragging
     */
    public boolean clickInSlot(InputManager manager, Slot slot) {
        if (item == null)
            return false;
        return item.clickInSlot(manager, slot, this);
    }
}
