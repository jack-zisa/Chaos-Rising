package dev.creoii.chaos.util.provider.booleanprovider;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record BetweenNumberBooleanProvider(NumberProvider value, NumberProvider min, NumberProvider max) implements BooleanProvider {
    public static final MapCodec<BetweenNumberBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(BetweenNumberBooleanProvider::value),
            NumberProvider.CODEC.fieldOf("min").forGetter(BetweenNumberBooleanProvider::min),
            NumberProvider.CODEC.fieldOf("max").forGetter(BetweenNumberBooleanProvider::max)
        ).apply(instance, BetweenNumberBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.BETWEEN_NUMBER;
    }

    @Override
    public Boolean get(ContextProvider context) {
        float val = value.get(context);
        return min.get(context) < val && val < max.get(context);
    }

    @Override
    public Provider<Boolean> optimize() {
        if (value instanceof ConstantNumberProvider(float value1) && min instanceof ConstantNumberProvider(float min1) && max instanceof ConstantNumberProvider(float max1)) {
            return new ConstantBooleanProvider(min1 < value1 && value1 < max1);
        }
        return BooleanProvider.super.optimize();
    }

    @Override
    public BetweenNumberBooleanProvider copy() {
        return new BetweenNumberBooleanProvider(value.copy(), min.copy(), max.copy());
    }

    public BetweenNumberBooleanProvider init(int startTime) {
        value.init(startTime);
        min.init(startTime);
        max.init(startTime);
        return this;
    }
}
