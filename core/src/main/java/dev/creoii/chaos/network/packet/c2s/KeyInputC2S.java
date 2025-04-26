package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record KeyInputC2S(UUID uuid, Action action, int keycode) implements Serializable {
    public enum Action {
        DOWN,
        UP,
        HELD
    }
}
