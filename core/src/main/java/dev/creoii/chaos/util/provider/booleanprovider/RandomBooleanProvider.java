package dev.creoii.chaos.util.provider.booleanprovider;

public record RandomBooleanProvider() implements BooleanProvider {
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
