package dev.creoii.chaos.util.provider.intprovider;

public class TrigIntProvider implements IntProvider {
    public enum Function { SIN, COS, TAN }

    private final Function function;
    private final IntProvider value;

    public TrigIntProvider(Function function, IntProvider value) {
        this.function = function;
        this.value = value;
    }

    @Override
    public Integer get(Context context) {
        int v = value.get(context);
        return switch (function) {
            case SIN -> (int) Math.sin(v);
            case COS -> (int) Math.cos(v);
            case TAN -> (int) Math.tan(v);
        };
    }

    @Override
    public IntProvider copy() {
        return new TrigIntProvider(function, value.copy());
    }
}
