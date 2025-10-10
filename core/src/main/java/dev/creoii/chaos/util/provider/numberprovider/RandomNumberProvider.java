package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RandomNumberProvider(NumberProvider min, NumberProvider max) implements NumberProvider {
    public static final MapCodec<RandomNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("min").forGetter(RandomNumberProvider::min),
            NumberProvider.CODEC.fieldOf("max").forGetter(RandomNumberProvider::max)
        ).apply(instance, RandomNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public Float get(Context context) {
        float max = this.max.get(context);
        float min = this.min.get(context);
        return context.random().nextFloat() * (max - min) + min;
    }

    @Override
    public RandomNumberProvider copy() {
        return new RandomNumberProvider(min.copy(), max.copy());
    }

    @Override
    public RandomNumberProvider init(int startTime) {
        min.init(startTime);
        max.init(startTime);
        return this;
    }
}

