package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Operation;

public record BinaryNumberProvider(NumberProvider a, NumberProvider b, Operation operation) implements NumberProvider {
    public static final MapCodec<BinaryNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b),
            Operation.CODEC.fieldOf("operation").orElse(Operation.ADD).forGetter(BinaryNumberProvider::operation)
        ).apply(instance, BinaryNumberProvider::new);
    });
    public static final MapCodec<BinaryNumberProvider> ADD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.ADD));
    });
    public static final MapCodec<BinaryNumberProvider> SUB_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.SUB));
    });
    public static final MapCodec<BinaryNumberProvider> MUL_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.MUL));
    });
    public static final MapCodec<BinaryNumberProvider> DIV_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.DIV));
    });
    public static final MapCodec<BinaryNumberProvider> MOD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.MOD));
    });
    public static final MapCodec<BinaryNumberProvider> POW_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(BinaryNumberProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(BinaryNumberProvider::b)
        ).apply(instance, (a, b) -> new BinaryNumberProvider(a, b, Operation.POW));
    });

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Float get(Context context) {
        float av = a.get(context), bv = b.get(context);
        return switch (operation) {
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
        return new BinaryNumberProvider(a.copy(), b.copy(), operation);
    }

    @Override
    public BinaryNumberProvider init(int startTime) {
        a.init(startTime);
        b.init(startTime);
        return this;
    }
}
