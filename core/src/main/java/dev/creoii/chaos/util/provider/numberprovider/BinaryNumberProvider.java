package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.Operation;

public class BinaryNumberProvider implements NumberProvider {
    private final NumberProvider a, b;
    private final Operation op;

    public BinaryNumberProvider(NumberProvider a, NumberProvider b, Operation op) {
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
            case MOD -> av % bv;
        };
    }

    @Override
    public BinaryNumberProvider copy() {
        return new BinaryNumberProvider(a.copy(), b.copy(), op);
    }

    @Override
    public BinaryNumberProvider init(int startTime) {
        a.init(startTime);
        b.init(startTime);
        return this;
    }
}
