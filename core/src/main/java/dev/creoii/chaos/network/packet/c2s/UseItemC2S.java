package dev.creoii.chaos.network.packet.c2s;

import dev.creoii.chaos.inventory.SlotEntry;

import java.io.Serializable;
import java.util.UUID;

public record UseItemC2S(UUID uuid, SlotEntry slot) implements Serializable {
}
