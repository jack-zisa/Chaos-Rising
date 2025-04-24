package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.util.provider.BinaryOperation;

public class BinaryBooleanProvider implements BooleanProvider {
    private final BooleanProvider a;
    private final BooleanProvider b;
    private final BinaryOperation operation;

    public BinaryBooleanProvider(BooleanProvider a, BooleanProvider b, BinaryOperation operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

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
