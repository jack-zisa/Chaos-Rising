package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record LootDropCloseS2C() implements Serializable {
    private static final LootDropCloseS2C INSTANCE = new LootDropCloseS2C();
    public static final Codec<LootDropCloseS2C> CODEC = Codec.unit(INSTANCE);
}
