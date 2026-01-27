package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record RequestWorldLoadC2S() {
    private static final RequestWorldLoadC2S INSTANCE = new RequestWorldLoadC2S();
    public static final Codec<RequestWorldLoadC2S> CODEC = Codec.unit(INSTANCE);

    public static void write(Output output, RequestWorldLoadC2S o) {
    }

    public static RequestWorldLoadC2S read(Input input) {
        return INSTANCE;
    }
}
