package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record CharacterJoinC2S(UUID uuid) implements Serializable {
}
