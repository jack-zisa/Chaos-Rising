package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class AbilityItem extends EquipmentItem {
    public static final MapCodec<AbilityItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(AbilityItem::id),
            Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(AbilityItem::getRarity),
            ModifierEntry.CODEC.listOf().fieldOf("stat_bonus").orElse(List.of()).forGetter(AbilityItem::getStatBonus),
            Codec.INT.fieldOf("cooldown").orElse(0).forGetter(AbilityItem::getCooldown)
        ).apply(instance, (id, rarity, statBonus, cooldown) -> new AbilityItem(id, rarity, statBonus, null, cooldown));
    });
    private final Attack attack;
    private final int cooldown;

    public AbilityItem(String id, Rarity rarity, List<ModifierEntry> statBonus, Attack attack, int cooldown) {
        super(id, Type.ABILITY, rarity, statBonus);
        this.attack = attack;
        this.cooldown = cooldown;
    }

    public Attack getAttack() {
        return attack;
    }

    public int getCooldown() {
        return cooldown;
    }
}
