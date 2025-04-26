package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record KeyInputC2S(UUID uuid, Action action, int keycode) {
    public enum Action {
        DOWN,
        UP,
        HELD
    }
}
