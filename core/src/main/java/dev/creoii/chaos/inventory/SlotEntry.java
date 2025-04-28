package dev.creoii.chaos.inventory;

import java.io.Serializable;

public record SlotEntry(int r, int c, String id, int count) implements Serializable {
}
