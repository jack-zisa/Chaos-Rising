package dev.creoii.chaos.network.packet.s2c;

import java.io.Serializable;
import java.util.UUID;

public record EntityStateS2C(UUID uuid, float x, float y) implements Serializable {
}
