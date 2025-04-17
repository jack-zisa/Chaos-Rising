package dev.creoii.chaos.item;

import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class EquipmentItem extends Item {
    protected final StatModifier statModifier;

    public EquipmentItem(Type type, Rarity rarity, String textureId, StatModifier statModifier) {
        super(type, rarity, textureId);
        this.statModifier = statModifier;
    }

    public StatModifier getStatModifier() {
        return statModifier;
    }
}
