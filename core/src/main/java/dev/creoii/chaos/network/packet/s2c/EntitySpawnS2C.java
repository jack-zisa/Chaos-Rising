package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.entity.Entity;

import java.io.Serializable;

public record EntitySpawnS2C(Entity entity) implements Serializable {
}
