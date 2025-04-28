package dev.creoii.chaos.network.packet.c2s;

import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;

import java.io.Serializable;
import java.util.UUID;

public record SlotUpdateC2S(UUID uuid, Action action, InventoryType from, InventoryType to, SlotEntry fromSlot, SlotEntry toSlot) implements Serializable {
    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE
    }
}
