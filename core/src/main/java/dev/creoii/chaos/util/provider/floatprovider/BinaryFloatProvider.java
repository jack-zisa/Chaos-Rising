package dev.creoii.chaos.util.provider.floatprovider;

public class BinaryFloatProvider implements FloatProvider {
    public enum Operation { ADD, SUB, MUL, DIV }

    private final FloatProvider a, b;
    private final Operation op;

    public BinaryFloatProvider(FloatProvider a, FloatProvider b, Operation op) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    @Override
    public Float get(Context context) {
        float av = a.get(context), bv = b.get(context);
        return switch (op) {
            case ADD -> av + bv;
            case SUB -> av - bv;
            case MUL -> av * bv;
            case DIV -> av / bv;
        };
    }

    @Override
    public BinaryFloatProvider copy() {
        return new BinaryFloatProvider(a.copy(), b.copy(), op);
    }

    @Override
    public BinaryFloatProvider init(int startTime) {
        a.init(startTime);
        b.init(startTime);
        return this;
    }
}
