package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.util.provider.BinaryOperation;

public record BinaryBooleanProvider(BooleanProvider a, BooleanProvider b, BinaryOperation operation) implements BooleanProvider {
    @Override
    public Boolean get(Context context) {
        boolean av = a.get(context), bv = b.get(context);
        return switch (operation) {
            case AND -> av && bv;
            case OR -> av || bv;
            case XOR -> av ^ bv;
        };
    }

    @Override
    public BinaryBooleanProvider copy() {
        return new BinaryBooleanProvider(a.copy(), b.copy(), operation);
    }

    public BinaryBooleanProvider init(int startTime) {
        a.init(startTime);
        b.init(startTime);
        return this;
    }
}
