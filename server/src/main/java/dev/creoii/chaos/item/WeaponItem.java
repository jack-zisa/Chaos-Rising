package dev.creoii.chaos.item;

import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class WeaponItem extends EquipmentItem {
    private final Attack attack;

    public WeaponItem(String id, Rarity rarity, String textureId, Attack attack, List<ModifierEntry> statBonus) {
        super(id, Type.WEAPON, rarity, textureId, statBonus);
        this.attack = attack;
    }

    public Attack getAttack() {
        return attack;
    }
}
