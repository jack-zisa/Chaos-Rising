package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.PacketUtils;

import java.util.Optional;

public record CharacterJoinS2C(int id, float x, float y, EntityCustomData data) {
    public static final Codec<CharacterJoinS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(CharacterJoinS2C::id),
            Codec.FLOAT.optionalFieldOf("x").forGetter(entitySpawnS2C -> entitySpawnS2C.x == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.x)),
            Codec.FLOAT.optionalFieldOf("y").forGetter(entitySpawnS2C -> entitySpawnS2C.y == 0 ? Optional.empty() : Optional.of(entitySpawnS2C.y)),
            EntityCustomData.CODEC.fieldOf("data").forGetter(CharacterJoinS2C::data)
        ).apply(instance, (uuid, x, y, customData) -> new CharacterJoinS2C(uuid, x.orElse(0f), y.orElse(0f), customData));
    });

    public static void write(Output output, CharacterJoinS2C o) {
        output.writeInt(o.id);
        output.writeFloat(o.x);
        output.writeFloat(o.y);
        PacketUtils.writeCustomEntityData(output, o.data);
    }

    public static CharacterJoinS2C read(Input input) {
        return new CharacterJoinS2C(input.readInt(), input.readFloat(), input.readFloat(), PacketUtils.readCustomEntityData(input));
    }
}
