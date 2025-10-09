package dev.creoii.chaos.util.provider.booleanprovider;


import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record BetweenBooleanProvider(NumberProvider value, NumberProvider min, NumberProvider max) implements BooleanProvider {
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
