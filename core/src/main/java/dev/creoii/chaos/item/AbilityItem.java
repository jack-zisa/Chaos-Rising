package dev.creoii.chaos.item;

import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class AbilityItem extends EquipmentItem {
    private final Attack attack;
    private final int cooldown;

    public AbilityItem(Rarity rarity, String textureId, List<ModifierEntry> statBonus, Attack attack, int cooldown) {
        super(Type.ABILITY, rarity, textureId, statBonus);
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
