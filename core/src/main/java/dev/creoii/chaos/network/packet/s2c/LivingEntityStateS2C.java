package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public record LivingEntityStateS2C(UUID uuid, StatContainer stats, StatContainer maxStats) {
}
