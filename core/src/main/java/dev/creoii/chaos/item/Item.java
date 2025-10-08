package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Rarity;

import java.io.Serializable;
import java.util.UUID;

public class Item implements Identifiable, Serializable {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(Item::id),
            Codec.STRING.fieldOf("type").forGetter(item -> item.getType().name().toLowerCase()),
            Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(Item::getRarity)
        ).apply(instance, (id, type, rarity) -> new Item(id, Item.Type.valueOf(type.toUpperCase()), rarity));
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
