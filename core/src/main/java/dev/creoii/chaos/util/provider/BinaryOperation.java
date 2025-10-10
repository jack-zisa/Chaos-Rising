package dev.creoii.chaos.util.provider;

import com.mojang.serialization.Codec;

public enum BinaryOperation {
    AND,
    OR,
    XOR;

    public static final Codec<BinaryOperation> CODEC = Codec.STRING.xmap(s -> BinaryOperation.valueOf(s.toUpperCase()), operation -> operation.name().toLowerCase());
}
