package dev.creoii.chaos.item;

import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentItem extends Item {
    protected final List<ModifierEntry> statBonus;

    public EquipmentItem(Type type, Rarity rarity, String textureId, List<ModifierEntry> statBonus) {
        super(type, rarity, textureId);
        this.statBonus = statBonus;
    }

    public List<ModifierEntry> getStatBonus() {
        return statBonus;
    }

    public String getTooltip() {
        List<String> lines = new ArrayList<>();
        lines.add(id);
        lines.add(type.name().toLowerCase());
        if (statBonus != null) {
            lines.add(StatContainer.getTooltip(statBonus));
        }
        return String.join("\n", lines);
    }
}
