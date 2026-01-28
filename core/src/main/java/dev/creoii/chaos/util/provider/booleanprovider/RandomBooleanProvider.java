package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record RandomBooleanProvider() implements BooleanProvider {
    private static final RandomBooleanProvider INSTANCE = new RandomBooleanProvider();
    public static final MapCodec<RandomBooleanProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public Boolean get(ContextProvider context) {
        return context.has(ComponentTypes.RANDOM) && context.get(ComponentTypes.RANDOM).nextBoolean();
    }

    @Override
    public RandomBooleanProvider copy() {
        return new RandomBooleanProvider();
    }

    public RandomBooleanProvider init(int startTime) {
        return this;
    }
}
