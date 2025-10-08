package dev.creoii.chaos.inventory;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public enum InventoryType implements Serializable {
    MAIN,
    LOOT;

    public static final Codec<InventoryType> CODEC = Codec.STRING.xmap(s -> InventoryType.valueOf(s.toUpperCase()), inventoryType -> inventoryType.name().toLowerCase());
}
