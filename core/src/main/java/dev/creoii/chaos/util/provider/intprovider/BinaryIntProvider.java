package dev.creoii.chaos.util.provider.intprovider;

public class BinaryIntProvider implements IntProvider {
    public enum Operation { ADD, SUB, MUL, DIV }

    private final IntProvider a, b;
    private final Operation op;

    public BinaryIntProvider(IntProvider a, IntProvider b, Operation op) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    @Override
    public Integer get(Context context) {
        int av = a.get(context), bv = b.get(context);
        return switch (op) {
            case ADD -> av + bv;
            case SUB -> av - bv;
            case MUL -> av * bv;
            case DIV -> av / bv;
        };
    }

    @Override
    public IntProvider copy() {
        return new BinaryIntProvider(a.copy(), b.copy(), op);
    }
}
