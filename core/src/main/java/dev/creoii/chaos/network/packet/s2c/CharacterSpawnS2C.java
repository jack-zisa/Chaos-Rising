package dev.creoii.chaos.network.packet.s2c;

import java.util.UUID;

public record CharacterSpawnS2C(UUID uuid, String textureId, float x, float y, float scale) {
}
