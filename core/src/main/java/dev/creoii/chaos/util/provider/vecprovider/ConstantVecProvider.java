package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConstantVecProvider(Vector2 pos) implements VecProvider {
    public static final ConstantVecProvider ZERO = new ConstantVecProvider(Vector2.Zero.cpy());
    public static final MapCodec<ConstantVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.INT.fieldOf("x").orElse(0).forGetter(provider -> (int) provider.pos.x),
            Codec.INT.fieldOf("y").orElse(0).forGetter(provider -> (int) provider.pos.y)
        ).apply(instance, (x, y) -> {
            if (x == 0 && y == 0) {
                return ZERO;
            } else return new ConstantVecProvider(new Vector2(x, y));
        });
    });

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    public ConstantVecProvider(float x, float y) {
        this(new Vector2(x, y));
    }

    public ConstantVecProvider(float num) {
        this(num, num);
    }

    @Override
    public Vector2 get(Context context) {
        return pos;
    }

    @Override
    public VecProvider copy() {
        return this;
    }
}

