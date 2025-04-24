package dev.creoii.chaos.util.provider.numberprovider;

import javax.annotation.Nullable;

public class ClampNumberProvider implements NumberProvider {
    private final NumberProvider value;
    private final @Nullable NumberProvider min, max;

    public ClampNumberProvider(NumberProvider value, @Nullable NumberProvider min, @Nullable NumberProvider max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @Override
    public Float get(Context context) {
        float value = this.value.get(context);

        if (max != null) {
            value = Math.min(value, max.get(context));
        }

        if (min != null) {
            value = Math.max(value, min.get(context));
        }

        return value;
    }

    @Override
    public ClampNumberProvider copy() {
        return new ClampNumberProvider(value.copy(), min.copy(), max.copy());
    }

    @Override
    public ClampNumberProvider init(int startTime) {
        value.init(startTime);
        if (min != null)
            min.init(startTime);
        if (max != null)
            max.init(startTime);
        return this;
    }
}

