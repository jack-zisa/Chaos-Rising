package dev.creoii.chaos.util.provider;

import com.mojang.serialization.Codec;

public enum Comparison {
    LT,
    GT,
    LTEQ,
    GTEQ,
    NE,
    EQ;

    public static final Codec<Comparison> CODEC = Codec.STRING.xmap(s -> Comparison.valueOf(s.toUpperCase()), comparison -> comparison.name().toLowerCase());
}
