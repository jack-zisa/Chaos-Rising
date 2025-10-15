package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record LootDropOpenS2C(int id) {
    public static final Codec<LootDropOpenS2C> CODEC = Codec.INT.xmap(LootDropOpenS2C::new, LootDropOpenS2C::id);

    public static void write(Output output, LootDropOpenS2C o) {
        output.writeInt(o.id);
    }

    public static LootDropOpenS2C read(Input input) {
        return new LootDropOpenS2C(input.readInt());
    }
}
