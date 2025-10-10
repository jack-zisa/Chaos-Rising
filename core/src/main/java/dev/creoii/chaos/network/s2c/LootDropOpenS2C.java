package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record LootDropOpenS2C(int id) implements Serializable {
    public static final Codec<LootDropOpenS2C> CODEC = Codec.INT.xmap(LootDropOpenS2C::new, LootDropOpenS2C::id);
}
