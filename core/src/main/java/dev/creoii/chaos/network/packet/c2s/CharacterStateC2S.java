package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record CharacterStateC2S(UUID uuid, float x, float y) implements Serializable {
}
