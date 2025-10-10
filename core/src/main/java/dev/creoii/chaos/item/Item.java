package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Rarity;

import java.io.Serializable;

public class Item implements Identifiable, Serializable {
    public static final Codec<Item> CODEC = Type.CODEC.dispatch(Item::getType, group -> switch (group) {
        case WEAPON -> WeaponItem.CODEC;
        case ABILITY -> AbilityItem.CODEC;
        case ARMOR, ACCESSORY -> EquipmentItem.CODEC;
        case CONSUMABLE -> ConsumableItem.CODEC;
    });
    protected final String id;
    protected final Type type;
    protected final Rarity rarity;
    protected final ItemStack defaultStack;

    public Item(String id, Type type, Rarity rarity) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        defaultStack = new ItemStack(this);
    }

    @Override
    public String id() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack getDefaultStack() {
        return defaultStack;
    }

    public String getTooltip() {
        return id + "\n";
    }

    public boolean clickInSlot(Game game, int characterId, Slot slot, ItemStack stack) {
        return false;
    }

    public enum Type {
        WEAPON,
        ABILITY,
        ARMOR,
        ACCESSORY,
        CONSUMABLE;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
