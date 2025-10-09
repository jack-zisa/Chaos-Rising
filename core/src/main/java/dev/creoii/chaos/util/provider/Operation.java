package dev.creoii.chaos.util.provider;

import com.mojang.serialization.Codec;

public enum Operation {
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    POW;

    public static final Codec<Operation> CODEC = Codec.STRING.xmap(s -> Operation.valueOf(s.toUpperCase()), operation -> operation.name().toLowerCase());
}
