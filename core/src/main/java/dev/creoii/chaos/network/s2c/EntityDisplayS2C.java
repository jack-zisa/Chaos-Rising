package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EntityDisplayS2C(int id, String textureId, float scale) {
    public static final Codec<EntityDisplayS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(EntityDisplayS2C::id),
            Codec.STRING.fieldOf("texture_id").forGetter(EntityDisplayS2C::textureId),
            Codec.FLOAT.fieldOf("scale").forGetter(EntityDisplayS2C::scale)
        ).apply(instance, EntityDisplayS2C::new);
    });

    public static void write(Output output, EntityDisplayS2C o) {
        output.writeInt(o.id);
        output.writeString(o.textureId);
        output.writeFloat(o.scale);
    }

    public static EntityDisplayS2C read(Input input) {
        return new EntityDisplayS2C(input.readInt(), input.readString(), input.readFloat());
    }
}
