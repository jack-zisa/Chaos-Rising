package dev.creoii.chaos.item;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.inventory.Slot;

import javax.annotation.Nullable;
import java.util.UUID;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(null, 0);
    @Nullable
    private ServerItem item;
    private int count;

    public ItemStack(@Nullable ServerItem item, int count) {
        this.item = item;
        this.count = count;
    }

    public ItemStack(ServerItem item) {
        this(item, 1);
    }

    @Nullable
    public ServerItem getItem() {
        return item;
    }

    public void setItem(@Nullable ServerItem item) {
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
    public boolean clickInSlot(ServerGame game, UUID uuid, Slot slot) {
        if (item == null)
            return false;
        return item.clickInSlot(game, uuid, slot, this);
    }
}
