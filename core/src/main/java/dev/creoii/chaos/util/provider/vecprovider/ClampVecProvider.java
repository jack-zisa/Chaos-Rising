package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.Optional;

public record ClampVecProvider(VecProvider vec, Optional<NumberProvider> minX, Optional<NumberProvider> minY, Optional<NumberProvider> maxX, Optional<NumberProvider> maxY) implements VecProvider {
    public static final MapCodec<ClampVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("vec").forGetter(ClampVecProvider::vec),
            NumberProvider.CODEC.optionalFieldOf("minX").forGetter(ClampVecProvider::minX),
            NumberProvider.CODEC.optionalFieldOf("minY").forGetter(ClampVecProvider::minY),
            NumberProvider.CODEC.optionalFieldOf("maxX").forGetter(ClampVecProvider::maxX),
            NumberProvider.CODEC.optionalFieldOf("maxY").forGetter(ClampVecProvider::maxY)
        ).apply(instance, ClampVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.CLAMP;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        Vector2 v = vec.get(context);

        float x = v.x;
        float y = v.y;

        if (minX.isPresent())
            x = Math.max(x, minX.get().get(context));
        if (maxX.isPresent())
            x = Math.min(x, maxX.get().get(context));
        if (minY.isPresent())
            y = Math.max(y, minY.get().get(context));
        if (maxY.isPresent())
            y = Math.min(y, maxY.get().get(context));

        return new Vector2(x, y);
    }

    @Override
    public Provider<Vector2> optimize() {
        if (vec instanceof ConstantVecProvider(Vector2 pos)) {
            float x = pos.x;
            float y = pos.y;

            if (minX.isPresent() && minX.get() instanceof ConstantNumberProvider(float value))
                x = Math.max(x, value);
            if (maxX.isPresent() && maxX.get() instanceof ConstantNumberProvider(float value))
                x = Math.min(x, value);
            if (minY.isPresent() && minY.get() instanceof ConstantNumberProvider(float value))
                y = Math.max(y, value);
            if (maxY.isPresent() && maxY.get() instanceof ConstantNumberProvider(float value))
                y = Math.min(y, value);

            return new ConstantVecProvider(new Vector2(x, y));

        }
        return VecProvider.super.optimize();
    }

    @Override
    public VecProvider copy() {
        return new ClampVecProvider(vec.copy(), minX.map(NumberProvider::copy), minY.map(NumberProvider::copy), maxX.map(NumberProvider::copy), maxY.map(NumberProvider::copy));
    }
}
