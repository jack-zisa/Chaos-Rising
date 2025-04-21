package dev.creoii.chaos.util.provider.floatprovider;

public class TrigFloatProvider implements FloatProvider {
    public enum Function { SIN, COS, TAN }

    private final Function function;
    private final FloatProvider value;

    public TrigFloatProvider(Function function, FloatProvider value) {
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
    public TrigFloatProvider copy() {
        return new TrigFloatProvider(function, value.copy());
    }

    @Override
    public TrigFloatProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
