package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record MouseInputC2S(UUID uuid, int screenX, int screenY) {
}
