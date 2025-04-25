package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.network.packet.util.EntityGroup;

import java.util.UUID;

public record EntitySpawnS2C(UUID uuid, String textureId, EntityGroup group, float x, float y, float scale) {
}
