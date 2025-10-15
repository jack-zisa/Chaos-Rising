package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentItem extends Item {
    public static final MapCodec<EquipmentItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(EquipmentItem::id),
            Item.Type.CODEC.fieldOf("type").forGetter(EquipmentItem::getType),
            Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(EquipmentItem::getRarity),
            ModifierEntry.CODEC.listOf().fieldOf("stat_bonus").orElse(List.of()).forGetter(EquipmentItem::getStatBonus)
        ).apply(instance, EquipmentItem::new);
    });
    protected final List<ModifierEntry> statBonus;

    public EquipmentItem(String id, Type type, Rarity rarity, List<ModifierEntry> statBonus) {
        super(id, type, rarity);
        this.statBonus = statBonus;
    }

    public List<ModifierEntry> getStatBonus() {
        return statBonus;
    }

    public String getTooltip() {
        List<String> lines = new ArrayList<>();
        lines.add(id);
        lines.add(rarity.name());
        lines.add(type.name().toLowerCase());
        if (statBonus != null) {
            lines.add(StatContainer.getTooltip(statBonus));
        }
        return String.join("\n", lines);
    }
}
