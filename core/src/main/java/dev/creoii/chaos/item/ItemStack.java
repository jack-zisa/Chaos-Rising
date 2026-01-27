package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.inventory.Slot;

import javax.annotation.Nullable;

public class ItemStack {
    public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(stack -> stack.getItem() == null ? "" : stack.getItem().id()),
            Codec.INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(DataManager.getItem(id), count));
    });
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

    public boolean isEmpty() {
        return item == null || count <= 0 || this == EMPTY;
    }

    /**
     * @return false to allow dragging, true to disable dragging
     */
    public boolean clickInSlot(World world, int id, Slot slot) {
        if (item == null)
            return false;
        return item.clickInSlot(world, id, slot, this);
    }
}
