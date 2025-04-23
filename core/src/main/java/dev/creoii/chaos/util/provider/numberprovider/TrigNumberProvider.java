package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.TrigFunction;

public class TrigNumberProvider implements NumberProvider {
    private final TrigFunction function;
    private final NumberProvider value;

    public TrigNumberProvider(TrigFunction function, NumberProvider value) {
        this.function = function;
        this.value = value;
    }

    @Override
    public Float get(Context context) {
        float v = value.get(context);
        return switch (function) {
            case SIN -> (float) Math.sin(v);
            case COS -> (float) Math.cos(v);
            case TAN -> (float) Math.tan(v);
        };
    }

    @Override
    public TrigNumberProvider copy() {
        return new TrigNumberProvider(function, value.copy());
    }

    @Override
    public TrigNumberProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
