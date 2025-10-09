package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.Operation;

public record BinaryNumberProvider(NumberProvider a, NumberProvider b, Operation op) implements NumberProvider {

    @Override
    public Float get(Context context) {
        float av = a.get(context), bv = b.get(context);
        return switch (op) {
            case ADD -> av + bv;
            case SUB -> av - bv;
            case MUL -> av * bv;
            case DIV -> av / bv;
            case MOD -> av % bv;
            case POW -> (float) Math.pow(av, bv);
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
