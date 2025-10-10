package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record LootDropCloseC2S(int id) implements Serializable {
    public static final Codec<LootDropCloseC2S> CODEC = Codec.INT.xmap(LootDropCloseC2S::new, LootDropCloseC2S::id);
}
