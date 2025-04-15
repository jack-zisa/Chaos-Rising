package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

public class Slot {
    private final int x;
    private final int y;
    private ItemStack stack;

    public Slot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public boolean hasItem() {
        return stack != null && stack.getItem() != null;
    }

    public Slot copy() {
        Slot slot = new Slot(x, y);
        slot.setStack(stack);
        return slot;
    }
}
