package dev.creoii.chaos.network.packet.c2s;

import dev.creoii.chaos.inventory.Slot;

import java.util.UUID;

public record DropSlotItemC2S(UUID uuid, Slot slot) {
}
