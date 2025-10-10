package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MoveCharacterS2C(long packed) {
    public MoveCharacterS2C(float x, float y, float xv, float yv) {
        this(MoveEntitiesS2C.pack(x, y, xv, yv));
    }

    public static final Codec<MoveCharacterS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.LONG.fieldOf("packed").forGetter(MoveCharacterS2C::packed)
        ).apply(instance, MoveCharacterS2C::new);
    });
}
