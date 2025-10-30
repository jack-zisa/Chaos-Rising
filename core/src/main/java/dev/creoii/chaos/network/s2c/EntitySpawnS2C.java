package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.PacketUtils;

import java.util.Optional;

public record EntitySpawnS2C(int id, float x, float y, float scale, EntityCustomData customData) {
    public static final Codec<EntitySpawnS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(EntitySpawnS2C::id),
            Codec.FLOAT.optionalFieldOf("x").forGetter(entitySpawnS2C -> entitySpawnS2C.x == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.x)),
            Codec.FLOAT.optionalFieldOf("y").forGetter(entitySpawnS2C -> entitySpawnS2C.y == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.y)),
            Codec.FLOAT.fieldOf("scale").forGetter(EntitySpawnS2C::scale),
            EntityCustomData.CODEC.fieldOf("custom_data").forGetter(EntitySpawnS2C::customData)
        ).apply(instance, (id, x, y, scale, customData) -> new EntitySpawnS2C(id, x.orElse(0f), y.orElse(0f), scale, customData));
    });

    public static void write(Output output, EntitySpawnS2C o) {
        output.writeInt(o.id);
        output.writeFloat(o.x);
        output.writeFloat(o.y);
        output.writeFloat(o.scale);
        PacketUtils.writeCustomEntityData(output, o.customData);
    }

    public static EntitySpawnS2C read(Input input) {
        return new EntitySpawnS2C(input.readInt(), input.readFloat(), input.readFloat(), input.readFloat(), PacketUtils.readCustomEntityData(input));
    }
}
