package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    public Type getType() {
        return Type.CLAMP;
    }

    @Override
    public Float get(Context context) {
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

