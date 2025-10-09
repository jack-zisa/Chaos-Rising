package dev.creoii.chaos.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class WeaponItem extends EquipmentItem {
    public static final MapCodec<WeaponItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(WeaponItem::id),
            Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(WeaponItem::getRarity),
            ModifierEntry.CODEC.listOf().fieldOf("stat_bonus").orElse(List.of()).forGetter(WeaponItem::getStatBonus)
        ).apply(instance, (id, rarity, statBonus) -> new WeaponItem(id, rarity, null, statBonus));
    });
    private final Attack attack;

    public WeaponItem(String id, Rarity rarity, Attack attack, List<ModifierEntry> statBonus) {
        super(id, Type.WEAPON, rarity, statBonus);
        this.attack = attack;
    }

    public Attack getAttack() {
        return attack;
    }
}
