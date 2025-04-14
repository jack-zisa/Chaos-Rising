package dev.creoii.chaos.attack;

import dev.creoii.chaos.entity.Entity;

import java.util.Set;

public record MultiAttack(Set<Attack> attacks) implements Attack {
    @Override
    public void attack(Target target, Entity entity) {
        attacks.forEach(attack -> attack.attack(target, entity));
    }
}
