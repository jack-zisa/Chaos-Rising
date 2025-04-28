package dev.creoii.chaos.network.packet.s2c;

import java.io.Serializable;
import java.util.UUID;

public record LivingEntityStateS2C(UUID uuid, int health, int maxHealth, int speed, int maxSpeed) implements Serializable {
}
