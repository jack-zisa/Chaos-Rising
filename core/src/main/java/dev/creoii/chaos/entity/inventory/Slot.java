package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ItemStack;

public class Slot {
    private ItemStack stack;

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}
