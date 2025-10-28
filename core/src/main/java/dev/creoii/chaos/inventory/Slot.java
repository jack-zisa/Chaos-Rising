package dev.creoii.chaos.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Slot {
    public static final Codec<Slot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("r").forGetter(Slot::getR),
        Codec.INT.fieldOf("c").forGetter(Slot::getC),
        Slot.Type.CODEC.optionalFieldOf("type").forGetter(slot -> slot.type == Type.NONE ? Optional.empty() : Optional.of(slot.type)),
        ItemStack.CODEC.optionalFieldOf("stack").forGetter(slot -> slot.stack == ItemStack.EMPTY ? Optional.empty() : Optional.of(slot.stack))
    ).apply(instance, (r, c, type, stack) -> new Slot(r, c, type.orElse(Type.NONE), stack.orElse(ItemStack.EMPTY))));
    private final int r;
    private final int c;
    private Type type;
    private ItemStack stack;

    public Slot(int r, int c, Type type, ItemStack stack) {
        this.r = r;
        this.c = c;
        this.type = type;
        this.stack = stack;
    }

    public Slot(int r, int c, Type type) {
        this.r = r;
        this.c = c;
        this.type = type;
        this.stack = ItemStack.EMPTY;
    }

    public Slot(int r, int c) {
        this(r, c, Type.NONE);
    }

    public static List<List<Slot>> toSlotListArray(Slot[][] slots) {
        List<List<Slot>> slotEntries = new ArrayList<>();

        for (Slot[] slotsArr : slots) {
            List<Slot> entries = new ArrayList<>(Arrays.asList(slotsArr));
            slotEntries.add(entries);
        }

        return slotEntries;
    }

    public static Slot[][] toSlotArray(List<List<Slot>> list) {
        int r = list.size();
        int c = r > 0 ? list.getFirst().size() : 0;

        Slot[][] array = new Slot[r][c];

        for (int i = 0; i < r; ++i) {
            int size = list.get(i).size();
            for (int j = 0; j < size; ++j) {
                array[i][j] = list.get(i).get(j);
            }
        }

        return array;
    }

    public static Slot[][] createEmptySlotArray(int r, int c, BiFunction<Integer, Integer, Slot> creator) {
        Slot[][] array = new Slot[r][c];

        for (int ri = 0; ri < array.length; ++ri) {
            Slot[] slots = array[ri];
            for (int ci = 0; ci < slots.length; ++ci) {
                slots[ci] = creator.apply(ri, ci);
            }
        }

        return array;
    }

    public static Slot[][] createEmptySlotArray(int r, int c) {
        return createEmptySlotArray(r, c, Slot::new);
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
        setStack(ItemStack.EMPTY);
        return temp;
    }

    public boolean hasItem() {
        return stack != null && !stack.isEmpty() && stack.getCount() > 0;
    }

    public boolean canAccept(Item item) {
        return item != null && type.itemPredicate.test(item);
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
