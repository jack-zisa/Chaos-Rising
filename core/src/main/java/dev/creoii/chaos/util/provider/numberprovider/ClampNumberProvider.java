package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.Optional;

public record ClampNumberProvider(NumberProvider value, Optional<NumberProvider> min, Optional<NumberProvider> max) implements NumberProvider {
    public static final MapCodec<ClampNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(ClampNumberProvider::value),
            NumberProvider.CODEC.optionalFieldOf("min").forGetter(ClampNumberProvider::min),
            NumberProvider.CODEC.optionalFieldOf("max").forGetter(ClampNumberProvider::max)
        ).apply(instance, ClampNumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (value instanceof ConstantNumberProvider(float value1)) {
            if (max.isPresent() && max.get() instanceof ConstantNumberProvider(float value2)) {
                value1 = Math.min(value1, value2);
            }

            if (min.isPresent() && min.get() instanceof ConstantNumberProvider(float value2)) {
                value1 = Math.max(value1, value2);
            }

            return new ConstantNumberProvider(value1);
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.CLAMP;
    }

    @Override
    public Float get(ContextProvider context) {
        float value = this.value.get(context);

        if (max.isPresent()) {
            value = Math.min(value, max.get().get(context));
        }

        if (min.isPresent()) {
            value = Math.max(value, min.get().get(context));
        }

        return value;
    }

    @Override
    public ClampNumberProvider copy() {
        return new ClampNumberProvider(value.copy(), min.map(NumberProvider::copy), max.map(NumberProvider::copy));
    }

    @Override
    public ClampNumberProvider init(int startTime) {
        value.init(startTime);
        min.ifPresent(numberProvider -> numberProvider.init(startTime));
        max.ifPresent(numberProvider -> numberProvider.init(startTime));
        return this;
    }
}

