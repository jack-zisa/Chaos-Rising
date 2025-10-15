package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record LootDropCloseC2S(int id) {
    public static final Codec<LootDropCloseC2S> CODEC = Codec.INT.xmap(LootDropCloseC2S::new, LootDropCloseC2S::id);

    public static void write(Output output, LootDropCloseC2S o) {
        output.writeInt(o.id);
    }

    public static LootDropCloseC2S read(Input input) {
        return new LootDropCloseC2S(input.readInt());
    }
}
