package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.UnaryOperation;

public class UnaryNumberProvider implements NumberProvider {
    private final UnaryOperation function;
    private final NumberProvider value;

    public UnaryNumberProvider(UnaryOperation function, NumberProvider value) {
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
            case SQRT -> (float) Math.sqrt(v);
            case CBRT -> (float) Math.cbrt(v);
            case ABS -> Math.abs(v);
        };
    }

    @Override
    public UnaryNumberProvider copy() {
        return new UnaryNumberProvider(function, value.copy());
    }

    @Override
    public UnaryNumberProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
