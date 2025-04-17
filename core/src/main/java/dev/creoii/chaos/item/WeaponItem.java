package dev.creoii.chaos.item;

import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class WeaponItem extends Item {
    private final Attack attack;

    public WeaponItem(Rarity rarity, String textureId, Attack attack, StatModifier statModifier) {
        super(Type.WEAPON, rarity, textureId, statModifier);
        this.attack = attack;
    }

    public Attack getAttack() {
        return attack;
    }
}
