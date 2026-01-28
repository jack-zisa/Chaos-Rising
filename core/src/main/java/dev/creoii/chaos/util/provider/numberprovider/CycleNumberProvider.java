package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

public record CycleNumberProvider(NumberProvider value, NumberProvider max) implements NumberProvider {
    public static final MapCodec<CycleNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(CycleNumberProvider::value),
            NumberProvider.CODEC.fieldOf("max").forGetter(CycleNumberProvider::max)
        ).apply(instance, CycleNumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (value instanceof ConstantNumberProvider(float value1) && max instanceof ConstantNumberProvider(float value2)) {
            return new ConstantNumberProvider(value1 % value2);
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.CYCLE;
    }

    @Override
    public Float get(ContextProvider context) {
        return value.get(context) % max.get(context);
    }

    @Override
    public NumberProvider copy() {
        return new CycleNumberProvider(value.copy(), max.copy());
    }

    @Override
    public NumberProvider init(int startTime) {
        value.init(startTime);
        max.init(startTime);
        return this;
    }
}
