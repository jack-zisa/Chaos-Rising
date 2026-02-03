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

        return canAttack(attacker, item);
    }

    static float getAttacks(Attacker attacker, @Nullable EquipmentItem item) {
        float attackSpeed = attacker.getAttackSpeed();
        if (attackSpeed <= 0f)
            return 0f;

        float attacks = 1.5f + 6.5f * (attackSpeed / 75f);
        if (item instanceof WeaponItem weaponItem)
            attacks *= weaponItem.getRateOfFire();

        return 1000f / attacks;
    }

    static boolean canAttack(Attacker attacker, @Nullable EquipmentItem item) {
        return attacker.canAttack(getAttacks(attacker, item));
    }
}
