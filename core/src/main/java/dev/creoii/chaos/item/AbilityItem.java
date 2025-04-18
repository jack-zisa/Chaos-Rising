package dev.creoii.chaos.item;

import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class AbilityItem extends EquipmentItem {
    private final Attack attack;
    private final Attack.Target target;
    private final int cooldown;

    public AbilityItem(Rarity rarity, String textureId, StatModifier statModifier, Attack attack, Attack.Target target, int cooldown) {
        super(Type.ABILITY, rarity, textureId, statModifier);
        this.attack = attack;
        this.target = target;
        this.cooldown = cooldown;
    }

    public Attack getAttack() {
        return attack;
    }

    public Attack.Target getTarget() {
        return target;
    }

    public int getCooldown() {
        return cooldown;
    }
}
