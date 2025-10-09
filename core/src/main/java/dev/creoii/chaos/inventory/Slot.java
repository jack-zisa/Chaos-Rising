package dev.creoii.chaos.inventory;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Slot {
    private final int r;
    private final int c;
    private Type type;
    private ItemStack stack;
    private boolean active;

    public Slot(int r, int c, Type type, ItemStack stack, boolean active) {
        this.r = r;
        this.c = c;
        this.type = type;
        this.stack = stack;
        this.active = active;
    }

    public Slot(int r, int c, Type type) {
        this.r = r;
        this.c = c;
        this.type = type;
        this.stack = ItemStack.EMPTY;
        active = false;
    }

    public Slot(int r, int c) {
        this(r, c, Type.NONE);
    }

    public static List<List<SlotEntry>> toSlotEntries(Slot[][] slots) {
        List<List<SlotEntry>> slotEntries = new ArrayList<>();

        for (Slot[] slotsArr : slots) {
            List<SlotEntry> entries = new ArrayList<>();
            for (Slot slot : slotsArr) {
                entries.add(new SlotEntry(slot.r, slot.c, slot.type, slot.stack, slot.active));
            }
            slotEntries.add(entries);
        }

        return slotEntries;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean canAccept(Item item) {
        return type.itemPredicate.test(item);
    }

    public Slot copy() {
        Slot slot = new Slot(r, c);
        slot.setStack(stack);
        return slot;
    }

    public enum Type {
        NONE(_ -> true),
        WEAPON(item -> item.getType() == Item.Type.WEAPON),
        ABILITY(item -> item.getType() == Item.Type.ABILITY),
        ARMOR(item -> item.getType() == Item.Type.ARMOR),
        ACCESSORY(item -> item.getType() == Item.Type.ACCESSORY);

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
        private final Predicate<Item> itemPredicate;

        Type(Predicate<Item> itemPredicate) {
            this.itemPredicate = itemPredicate;
        }

        public Predicate<Item> getItemPredicate() {
            return itemPredicate;
        }
    }
}
