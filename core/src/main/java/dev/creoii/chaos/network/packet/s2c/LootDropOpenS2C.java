package dev.creoii.chaos.network.packet.s2c;

import java.io.Serializable;
import java.util.UUID;

public record LootDropOpenS2C(UUID uuid) implements Serializable {
}
