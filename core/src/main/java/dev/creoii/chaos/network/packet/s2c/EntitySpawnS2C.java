package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public record EntitySpawnS2C(UUID uuid, EntityGroup group, String textureId, float x, float y, float scale) {
}
