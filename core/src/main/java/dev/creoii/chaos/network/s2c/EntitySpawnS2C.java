package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.PacketUtils;

import java.io.Serializable;
import java.util.Optional;

public record EntitySpawnS2C(int id, float x, float y, EntityCustomData customData) implements Serializable {
    public static final Codec<EntitySpawnS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(EntitySpawnS2C::id),
            Codec.FLOAT.optionalFieldOf("x").forGetter(entitySpawnS2C -> entitySpawnS2C.x == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.x)),
            Codec.FLOAT.optionalFieldOf("y").forGetter(entitySpawnS2C -> entitySpawnS2C.y == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.y)),
            EntityCustomData.CODEC.fieldOf("custom_data").forGetter(EntitySpawnS2C::customData)
        ).apply(instance, (uuid, x, y, customData) -> new EntitySpawnS2C(uuid, x.orElse(0f), y.orElse(0f), customData));
    });

    public static void write(Output output, EntitySpawnS2C o) {
        output.writeInt(o.id);
        output.writeFloat(o.x);
        output.writeFloat(o.y);
        PacketUtils.writeCustomEntityData(output, o.customData);
    }

    public static EntitySpawnS2C read(Input input) {
        return new EntitySpawnS2C(input.readInt(), input.readFloat(), input.readFloat(), PacketUtils.readCustomEntityData(input));
    }
}
