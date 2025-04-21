package dev.creoii.chaos.attack;

import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.util.Set;

public record MultiAttack(Set<Attack> attacks) implements Attack {
    @Override
    public void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity) {
        attacks.forEach(attack -> attack.attack(targetPos, sourcePos, sourceEntity));
    }
}
