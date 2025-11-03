package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.BinaryOperation;
import dev.creoii.chaos.util.provider.Provider;

public record BinaryBooleanProvider(BooleanProvider a, BooleanProvider b, BinaryOperation operation) implements BooleanProvider {
    public static final MapCodec<BinaryBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("a").forGetter(BinaryBooleanProvider::a),
            BooleanProvider.CODEC.fieldOf("b").forGetter(BinaryBooleanProvider::b),
            BinaryOperation.CODEC.fieldOf("operation").orElse(BinaryOperation.AND).forGetter(BinaryBooleanProvider::operation)
        ).apply(instance, BinaryBooleanProvider::new);
    });
    public static final MapCodec<BinaryBooleanProvider> AND_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("a").forGetter(BinaryBooleanProvider::a),
            BooleanProvider.CODEC.fieldOf("b").forGetter(BinaryBooleanProvider::b)
        ).apply(instance, (a, b) -> new BinaryBooleanProvider(a, b, BinaryOperation.AND));
    });
    public static final MapCodec<BinaryBooleanProvider> OR_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("a").forGetter(BinaryBooleanProvider::a),
            BooleanProvider.CODEC.fieldOf("b").forGetter(BinaryBooleanProvider::b)
        ).apply(instance, (a, b) -> new BinaryBooleanProvider(a, b, BinaryOperation.OR));
    });
    public static final MapCodec<BinaryBooleanProvider> XOR_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("a").forGetter(BinaryBooleanProvider::a),
            BooleanProvider.CODEC.fieldOf("b").forGetter(BinaryBooleanProvider::b)
        ).apply(instance, (a, b) -> new BinaryBooleanProvider(a, b, BinaryOperation.XOR));
    });

    @Override
    public Type getType() {
        return Type.BINARY;
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
    public Provider<Boolean> optimize() {
        if (a instanceof ConstantBooleanProvider(boolean a1) && b instanceof ConstantBooleanProvider(boolean b1)) {
            return new ConstantBooleanProvider(switch (operation) {
                case AND -> a1 && b1;
                case OR -> a1 || b1;
                case XOR -> a1 ^ b1;
            });
        }
        return BooleanProvider.super.optimize();
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
