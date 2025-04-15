package dev.creoii.chaos.item;

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

    public boolean canStackWith(ItemStack other) {
        return other != null && other.item == this.item;
    }

    public void add(ItemStack other) {
        if (canStackWith(other)) {
            this.count += other.count;
        }
    }

    public void decrease(int amount) {
        this.count -= amount;
        if (count <= 0) {
            // remove stack
        }
    }
}
