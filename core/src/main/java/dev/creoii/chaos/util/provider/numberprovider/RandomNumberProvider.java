package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

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
    public Float get(ContextProvider context) {
        if (context.has(ComponentTypes.RANDOM)) {
            float max = this.max.get(context);
            float min = this.min.get(context);
            return context.get(ComponentTypes.RANDOM).nextFloat() * (max - min) + min;
        }
        return -1f;
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

