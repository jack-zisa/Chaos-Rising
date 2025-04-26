package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.item.ServerItem;
import dev.creoii.chaos.item.ItemStack;

import java.util.function.Predicate;

public class Slot {
    private final int r;
    private final int c;
    private Type type;
    private ItemStack stack;

    public Slot(int r, int c, Type type) {
        this.r = r;
        this.c = c;
        this.type = type;
    }

    public Slot(int r, int c) {
        this(r, c, Type.NONE);
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack takeStack() {
        ItemStack temp = stack.copy();
        setStack(null);
        return temp;
    }

    public boolean hasItem() {
        return stack != null && stack.getItem() != null;
    }

    public boolean canAccept(ServerItem item) {
        return type.itemPredicate.test(item);
    }

    public Slot copy() {
        Slot slot = new Slot(r, c);
        slot.setStack(stack);
        return slot;
    }

    public enum Type {
        NONE(item -> true),
        WEAPON(item -> item.getType() == ServerItem.Type.WEAPON),
        ABILITY(item -> item.getType() == ServerItem.Type.ABILITY),
        ARMOR(item -> item.getType() == ServerItem.Type.ARMOR),
        ACCESSORY(item -> item.getType() == ServerItem.Type.ACCESSORY);

        private final Predicate<ServerItem> itemPredicate;

        Type(Predicate<ServerItem> itemPredicate) {
            this.itemPredicate = itemPredicate;
        }

        public Predicate<ServerItem> getItemPredicate() {
            return itemPredicate;
        }
    }
}
