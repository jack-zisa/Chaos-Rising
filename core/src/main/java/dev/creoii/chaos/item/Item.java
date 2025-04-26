package dev.creoii.chaos.item;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Rarity;

import java.util.UUID;

public class Item implements Identifiable {
    protected final String id;
    protected final Type type;
    protected final Rarity rarity;

    public Item(String id, Type type, Rarity rarity) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
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

    public String getTooltip() {
        return id + "\n";
    }

    public boolean clickInSlot(Game game, UUID characterUuid, Slot slot, ItemStack stack) {
        return false;
    }

    public enum Type {
        WEAPON,
        ABILITY,
        ARMOR,
        ACCESSORY,
        CONSUMABLE
    }
}
