package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record KeyInputC2S(UUID uuid, int keydown, int keyheld, int keyup) {
}
