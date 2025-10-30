package dev.creoii.chaos.entity;

import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.item.WeaponItem;

import javax.annotation.Nullable;

public interface Attacker {
    float getAttackSpeed();

    long getLastAttackTime();

    void setLastAttackTime(long attackTime);

    default boolean canAttack(float cooldown) {
        return System.currentTimeMillis() - getLastAttackTime() >= cooldown;
    }

    static boolean canAttack(Entity entity) {
        return canAttack(entity, null);
    }

    static boolean canAttack(Entity entity, @Nullable EquipmentItem item) {
        if (!(entity instanceof Attacker attacker)) {
            return false;
        }

        float attackSpeed = attacker.getAttackSpeed();
        if (attackSpeed <= 0f)
            return false;

        float attacks = 1.5f + 6.5f * (attackSpeed / 75f);
        if (item instanceof WeaponItem weaponItem)
            attacks *= weaponItem.getRateOfFire();

        return attacker.canAttack(1000f / attacks);
    }
}
