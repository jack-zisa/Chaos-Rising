package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;

import java.io.Serializable;
import java.util.List;

public record InventoryUpdateS2C(InventoryType type, List<SlotEntry> slots) implements Serializable {
}
