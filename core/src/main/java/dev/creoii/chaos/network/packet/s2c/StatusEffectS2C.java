package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.effect.StatusEffect;

import java.io.Serializable;
import java.util.UUID;

public record StatusEffectS2C(UUID uuid, StatusEffect statusEffect) implements Serializable {
}
