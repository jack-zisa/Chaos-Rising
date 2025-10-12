package dev.creoii.chaos.entity;

public interface Attacker {
    float getAttackSpeed();

    long getLastAttackTime();

    void setLastAttackTime(long attackTime);

    default boolean canAttack(float cooldown) {
        return System.currentTimeMillis() - getLastAttackTime() >= cooldown;
    }
}
