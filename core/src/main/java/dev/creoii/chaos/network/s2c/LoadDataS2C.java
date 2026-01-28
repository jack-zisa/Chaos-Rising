package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record LoadDataS2C() {
    private static final LoadDataS2C INSTANCE = new LoadDataS2C();
    public static final Codec<LoadDataS2C> CODEC = Codec.unit(INSTANCE);

    public static void write(Output output, LoadDataS2C o) {
    }

    public static LoadDataS2C read(Input input) {
        return INSTANCE;
    }
}
