package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;

public record RandomBooleanProvider() implements BooleanProvider {
    private static final RandomBooleanProvider INSTANCE = new RandomBooleanProvider();
    public static final MapCodec<RandomBooleanProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public Boolean get(Context context) {
        return context.random().nextBoolean();
    }

    @Override
    public RandomBooleanProvider copy() {
        return new RandomBooleanProvider();
    }

    public RandomBooleanProvider init(int startTime) {
        return this;
    }
}
