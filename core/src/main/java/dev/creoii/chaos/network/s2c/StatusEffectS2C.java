package dev.creoii.chaos.network.s2c;

import dev.creoii.chaos.effect.StatusEffect;

import java.io.Serializable;

public record StatusEffectS2C(int id, StatusEffect statusEffect) implements Serializable {
}
