package dev.creoii.chaos.network.packet.c2s;

import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;

import java.io.Serializable;
import java.util.UUID;

public record SlotUpdateC2S(UUID uuid, Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot) implements Serializable {
    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE
    }
}
