package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.tooltip.Tooltip;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Rarity;

public class Item implements Identifiable {
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
    protected final Tooltip tooltip;

    public Item(String id, Type type, Rarity rarity) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        defaultStack = new ItemStack(this);

        Tooltip tooltip1 = new Tooltip();
        buildTooltip(tooltip1);
        tooltip1 = tooltip1.order();
        tooltip = tooltip1;
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

    public void buildTooltip(Tooltip tooltip) {
        tooltip.addSection(Tooltip.Section.NAME, id);
        tooltip.addSection(Tooltip.Section.TYPE, type.name());
        tooltip.addSection(Tooltip.Section.RARITY, rarity.name());
    }

    public Tooltip getTooltip() {
        return tooltip;
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
