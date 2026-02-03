package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record SyncAttacksS2C(float attacks) {
    public static final Codec<SyncAttacksS2C> CODEC = Codec.FLOAT.xmap(SyncAttacksS2C::new, SyncAttacksS2C::attacks);

    public static void write(Output output, SyncAttacksS2C o) {
        output.writeFloat(o.attacks);
    }

    public static SyncAttacksS2C read(Input input) {
        return new SyncAttacksS2C(input.readFloat());
    }
}
