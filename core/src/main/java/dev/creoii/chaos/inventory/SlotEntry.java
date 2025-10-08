package dev.creoii.chaos.inventory;

import dev.creoii.chaos.item.ItemStack;

import java.io.Serializable;

public record SlotEntry(int r, int c, ItemStack stack) implements Serializable {
}
