package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record DropSlotItemC2S(UUID uuid, int slotR, int slotC) {
}
