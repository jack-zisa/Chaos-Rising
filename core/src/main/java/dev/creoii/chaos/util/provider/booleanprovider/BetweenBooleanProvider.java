package dev.creoii.chaos.util.provider.booleanprovider;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record BetweenBooleanProvider(NumberProvider value, NumberProvider min, NumberProvider max) implements BooleanProvider {
    public static final MapCodec<BetweenBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(BetweenBooleanProvider::value),
            NumberProvider.CODEC.fieldOf("min").forGetter(BetweenBooleanProvider::min),
            NumberProvider.CODEC.fieldOf("max").forGetter(BetweenBooleanProvider::max)
        ).apply(instance, BetweenBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.BETWEEN;
    }

    @Override
    public Boolean get(Context context) {
        float val = value.get(context);
        return min.get(context) < val && val < max.get(context);
    }

    @Override
    public BetweenBooleanProvider copy() {
        return new BetweenBooleanProvider(value.copy(), min.copy(), max.copy());
    }

    public BetweenBooleanProvider init(int startTime) {
        value.init(startTime);
        min.init(startTime);
        max.init(startTime);
        return this;
    }
}
