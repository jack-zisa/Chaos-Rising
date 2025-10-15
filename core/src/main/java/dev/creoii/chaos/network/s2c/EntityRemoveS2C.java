package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record EntityRemoveS2C(int id) {
    public static final Codec<EntityRemoveS2C> CODEC = Codec.INT.xmap(EntityRemoveS2C::new, EntityRemoveS2C::id);

    public static void write(Output output, EntityRemoveS2C o) {
        output.writeInt(o.id);
    }

    public static EntityRemoveS2C read(Input input) {
        return new EntityRemoveS2C(input.readInt());
    }
}
