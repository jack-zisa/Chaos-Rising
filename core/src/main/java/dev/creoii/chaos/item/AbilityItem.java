package dev.creoii.chaos.item;

import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class AbilityItem extends EquipmentItem {
    private final Attack attack;
    private final int cooldown;

    public AbilityItem(Rarity rarity, String textureId, StatModifier statModifier, Attack attack, int cooldown) {
        super(Type.ABILITY, rarity, textureId, statModifier);
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
