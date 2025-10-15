package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record LootDropCloseS2C() {
    private static final LootDropCloseS2C INSTANCE = new LootDropCloseS2C();
    public static final Codec<LootDropCloseS2C> CODEC = Codec.unit(INSTANCE);

    public static void write(Output output, LootDropCloseS2C o) {

    }

    public static LootDropCloseS2C read(Input input) {
        return INSTANCE;
    }
}
