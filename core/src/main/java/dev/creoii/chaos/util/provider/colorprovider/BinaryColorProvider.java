package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Operation;

public record BinaryColorProvider(ColorProvider a, ColorProvider b, Operation operation) implements ColorProvider {
    public static final MapCodec<BinaryColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b),
            Operation.CODEC.fieldOf("operation").orElse(Operation.ADD).forGetter(BinaryColorProvider::operation)
        ).apply(instance, BinaryColorProvider::new);
    });
    public static final MapCodec<BinaryColorProvider> ADD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.ADD));
    });
    public static final MapCodec<BinaryColorProvider> SUB_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.SUB));
    });
    public static final MapCodec<BinaryColorProvider> MUL_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.MUL));
    });
    public static final MapCodec<BinaryColorProvider> DIV_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.DIV));
    });
    public static final MapCodec<BinaryColorProvider> MOD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.MOD));
    });
    public static final MapCodec<BinaryColorProvider> POW_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BinaryColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BinaryColorProvider::b)
        ).apply(instance, (a, b) -> new BinaryColorProvider(a, b, Operation.POW));
    });

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Color get(ContextProvider context) {
        Color av = a.get(context), bv = b.get(context);
        return switch (operation) {
            case ADD -> av.add(bv);
            case SUB -> av.sub(bv);
            case MUL -> av.mul(bv);
            case DIV -> div(av, bv);
            case MOD -> mod(av, bv);
            case POW -> pow(av, bv);
        };
    }

    public Color div(Color a, Color b) {
        a.r /= b.r;
        a.g /= b.g;
        a.b /= b.b;
        a.a /= b.a;
        return a.clamp();
    }

    public Color mod(Color a, Color b) {
        a.r %= b.r;
        a.g %= b.g;
        a.b %= b.b;
        a.a %= b.a;
        return a.clamp();
    }

    public Color pow(Color a, Color b) {
        a.r = (float) Math.pow(a.r, b.r);
        a.g = (float) Math.pow(a.g, b.g);
        a.b = (float) Math.pow(a.b, b.b);
        a.a = (float) Math.pow(a.a, b.a);
        return a.clamp();
    }
}
