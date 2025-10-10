package dev.creoii.chaos.util.provider;

import com.mojang.serialization.Codec;

public enum UnaryOperation {
    SIN,
    COS,
    TAN,
    SQRT,
    CBRT,
    ABS;

    public static final Codec<UnaryOperation> CODEC = Codec.STRING.xmap(s -> UnaryOperation.valueOf(s.toUpperCase()), operation -> operation.name().toLowerCase());
}
