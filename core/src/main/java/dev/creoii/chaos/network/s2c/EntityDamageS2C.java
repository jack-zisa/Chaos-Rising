package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EntityDamageS2C(int id, float amount) {
    public static final Codec<EntityDamageS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(EntityDamageS2C::id),
            Codec.FLOAT.fieldOf("amount").forGetter(EntityDamageS2C::amount)
        ).apply(instance, EntityDamageS2C::new);
    });

    public static void write(Output output, EntityDamageS2C o) {
        output.writeInt(o.id);
        output.writeFloat(o.amount);
    }

    public static EntityDamageS2C read(Input input) {
        return new EntityDamageS2C(input.readInt(), input.readFloat());
    }
}
