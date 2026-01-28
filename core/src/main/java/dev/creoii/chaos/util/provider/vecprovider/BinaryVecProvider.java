package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Operation;
import dev.creoii.chaos.util.provider.Provider;

public record BinaryVecProvider(VecProvider a, VecProvider b, Operation operation) implements VecProvider {
    public static final MapCodec<BinaryVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b),
            Operation.CODEC.fieldOf("operation").orElse(Operation.ADD).forGetter(BinaryVecProvider::operation)
        ).apply(instance, BinaryVecProvider::new);
    });
    public static final MapCodec<BinaryVecProvider> ADD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.ADD));
    });
    public static final MapCodec<BinaryVecProvider> SUB_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.SUB));
    });
    public static final MapCodec<BinaryVecProvider> MUL_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.MUL));
    });
    public static final MapCodec<BinaryVecProvider> DIV_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.DIV));
    });
    public static final MapCodec<BinaryVecProvider> MOD_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.MOD));
    });
    public static final MapCodec<BinaryVecProvider> POW_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(BinaryVecProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(BinaryVecProvider::b)
        ).apply(instance, (a, b) -> new BinaryVecProvider(a, b, Operation.POW));
    });

    @Override
    public Provider<Vector2> optimize() {
        if (a instanceof ConstantVecProvider(Vector2 pos) && b instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantVecProvider(switch (operation) {
                case ADD -> pos.add(pos1);
                case SUB -> pos.sub(pos1);
                case MUL -> pos.scl(pos1);
                case DIV -> {
                    float x = pos.x / pos1.x;
                    float y = pos.y / pos1.y;
                    yield new Vector2(x, y);
                }
                case MOD -> {
                    float x = pos.x % pos1.x;
                    float y = pos.y % pos1.y;
                    yield new Vector2(x, y);
                }
                case POW -> {
                    float x = (float) Math.pow(pos.x, pos1.x);
                    float y = (float) Math.pow(pos.y, pos1.y);
                    yield new Vector2(x, y);
                }
            });
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        Vector2 av = a.get(context), bv = b.get(context);
        return switch (operation) {
            case ADD -> av.add(bv);
            case SUB -> av.sub(bv);
            case MUL -> av.scl(bv);
            case DIV -> {
                float x = av.x / bv.x;
                float y = av.y / bv.y;
                yield new Vector2(x, y);
            }
            case MOD -> {
                float x = av.x % bv.x;
                float y = av.y % bv.y;
                yield new Vector2(x, y);
            }
            case POW -> {
                float x = (float) Math.pow(av.x, bv.x);
                float y = (float) Math.pow(av.y, bv.y);
                yield new Vector2(x, y);
            }
        };
    }

    @Override
    public VecProvider copy() {
        return new BinaryVecProvider(a.copy(), b.copy(), operation);
    }
}
