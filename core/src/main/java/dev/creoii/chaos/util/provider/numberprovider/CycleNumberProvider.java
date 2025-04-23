package dev.creoii.chaos.util.provider.numberprovider;

public class CycleNumberProvider implements NumberProvider {
    private final NumberProvider value;
    private final NumberProvider max;

    public CycleNumberProvider(NumberProvider value, NumberProvider max) {
        this.value = value;
        this.max = max;
    }

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
