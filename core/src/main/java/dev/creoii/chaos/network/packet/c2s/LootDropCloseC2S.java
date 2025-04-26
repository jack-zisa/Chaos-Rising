package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record LootDropCloseC2S(UUID uuid) implements Serializable {
}
