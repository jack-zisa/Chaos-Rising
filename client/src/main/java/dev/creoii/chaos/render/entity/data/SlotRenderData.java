package dev.creoii.chaos.render.entity.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;

public class SlotRenderData {
    public ItemStack stack;
    public Slot.Type type;

    public SlotRenderData(ItemStack stack, Slot.Type type) {
        this.stack = stack;
        this.type = type;
    }
}
