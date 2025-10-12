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
            Attack.CODEC.fieldOf("attack").forGetter(WeaponItem::getAttack),
            Codec.FLOAT.fieldOf("rate_of_fire").orElse(1f).forGetter(WeaponItem::getRateOfFire),
            ModifierEntry.CODEC.listOf().fieldOf("stat_bonus").orElse(List.of()).forGetter(WeaponItem::getStatBonus)
        ).apply(instance, WeaponItem::new);
    });
    private final Attack attack;
    private final float rateOfFire;

    public WeaponItem(String id, Rarity rarity, Attack attack, float rateOfFire, List<ModifierEntry> statBonus) {
        super(id, Type.WEAPON, rarity, statBonus);
        this.attack = attack;
        this.rateOfFire = rateOfFire;
    }

    public Attack getAttack() {
        return attack;
    }

    public float getRateOfFire() {
        return rateOfFire;
    }
}
