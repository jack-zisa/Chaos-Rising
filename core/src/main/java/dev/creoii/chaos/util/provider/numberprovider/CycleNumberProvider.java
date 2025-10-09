package dev.creoii.chaos.util.provider.numberprovider;

public record CycleNumberProvider(NumberProvider value, NumberProvider max) implements NumberProvider {

    @Override
    public Float get(Context context) {
        return value.get(context) % max.get(context);
    }

    @Override
    public NumberProvider copy() {
        return new CycleNumberProvider(value.copy(), max.copy());
    }

    @Override
    public NumberProvider init(int startTime) {
        value.init(startTime);
        max.init(startTime);
        return this;
    }
}
