package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.Objects;

public record ConstantVecProvider(NumberProvider x, NumberProvider y) implements VecProvider {
    public static final ConstantVecProvider ZERO = new ConstantVecProvider(0, 0);
    public static final MapCodec<ConstantVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("x").orElse(ConstantNumberProvider.ZERO).forGetter(ConstantVecProvider::x),
            NumberProvider.CODEC.fieldOf("y").orElse(ConstantNumberProvider.ZERO).forGetter(ConstantVecProvider::y)
        ).apply(instance, ConstantVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    public ConstantVecProvider(Vector2 vector2) {
        this(new ConstantNumberProvider(vector2.x), new ConstantNumberProvider(vector2.y));
    }

    public ConstantVecProvider(float x, float y) {
        this(new ConstantNumberProvider(x), new ConstantNumberProvider(y));
    }

    public ConstantVecProvider(float num) {
        this(num, num);
    }

    @Override
    public Vector2 get(Context context) {
        return new Vector2(x.get(context), Objects.requireNonNullElse(y, x).get(context));
    }

    @Override
    public VecProvider copy() {
        return this;
    }
}

