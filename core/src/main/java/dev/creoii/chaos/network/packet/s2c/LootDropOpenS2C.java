package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.inventory.Inventory;

import java.io.Serializable;

public record LootDropOpenS2C(Inventory inventory) implements Serializable {
}
