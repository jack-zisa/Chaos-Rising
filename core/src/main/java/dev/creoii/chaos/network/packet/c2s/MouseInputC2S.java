package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record MouseInputC2S(UUID uuid, Action action, int screenX, int screenY) {
    public enum Action {
        DOWN,
        UP,
        CANCEL,
        DRAG
    }
}
