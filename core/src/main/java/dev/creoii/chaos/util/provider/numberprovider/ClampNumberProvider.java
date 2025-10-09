package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import javax.annotation.Nullable;

public record ClampNumberProvider(NumberProvider value, @Nullable NumberProvider min, @Nullable NumberProvider max) implements NumberProvider {
    public static final MapCodec<ClampNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(ClampNumberProvider::value),
            NumberProvider.CODEC.fieldOf("min").forGetter(ClampNumberProvider::min),
            NumberProvider.CODEC.fieldOf("max").forGetter(ClampNumberProvider::max)
        ).apply(instance, ClampNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.CLAMP;
    }

    @Override
    public Float get(Context context) {
        float value = this.value.get(context);

        if (max != null) {
            value = Math.min(value, max.get(context));
        }

        if (min != null) {
            value = Math.max(value, min.get(context));
        }

        return value;
    }

    @Override
    public ClampNumberProvider copy() {
        return new ClampNumberProvider(value.copy(), min.copy(), max.copy());
    }

    @Override
    public ClampNumberProvider init(int startTime) {
        value.init(startTime);
        if (min != null)
            min.init(startTime);
        if (max != null)
            max.init(startTime);
        return this;
    }
}

