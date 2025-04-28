package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record CharacterMoveC2S(UUID uuid, float dx, float dy) implements Serializable {
}
