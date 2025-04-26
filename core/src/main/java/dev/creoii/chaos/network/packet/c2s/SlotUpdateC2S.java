package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record SlotUpdateC2S(UUID uuid, Action action, int fromR, int fromC, int toR, int toC) {
    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE
    }
}
