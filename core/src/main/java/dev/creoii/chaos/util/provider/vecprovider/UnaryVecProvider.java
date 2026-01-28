package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryVecProvider(UnaryOperation operation, VecProvider value) implements VecProvider {
    public static final MapCodec<UnaryVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            UnaryOperation.CODEC.fieldOf("operation").orElse(UnaryOperation.SIN).forGetter(UnaryVecProvider::operation),
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, UnaryVecProvider::new);
    });
    public static final MapCodec<UnaryVecProvider> SIN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.SIN, value));
    });
    public static final MapCodec<UnaryVecProvider> COS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.COS, value));
    });
    public static final MapCodec<UnaryVecProvider> TAN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.TAN, value));
    });
    public static final MapCodec<UnaryVecProvider> SQRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.SQRT, value));
    });
    public static final MapCodec<UnaryVecProvider> CBRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.CBRT, value));
    });
    public static final MapCodec<UnaryVecProvider> ABS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, value -> new UnaryVecProvider(UnaryOperation.ABS, value));
    });

    @Override
    public Provider<Vector2> optimize() {
        if (value instanceof ConstantVecProvider(Vector2 pos)) {
            return new ConstantVecProvider(switch (operation) {
                case SIN -> new Vector2((float) Math.sin(pos.x), (float) Math.sin(pos.y));
                case COS -> new Vector2((float) Math.cos(pos.x), (float) Math.cos(pos.y));
                case TAN -> new Vector2((float) Math.tan(pos.x), (float) Math.tan(pos.y));
                case SQRT -> new Vector2((float) Math.sqrt(pos.x), (float) Math.sqrt(pos.y));
                case CBRT -> new Vector2((float) Math.cbrt(pos.x), (float) Math.cbrt(pos.y));
                case ABS -> new Vector2(Math.abs(pos.x), Math.abs(pos.y));
            });
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.UNARY;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        Vector2 v = value.get(context);
        return switch (operation) {
            case SIN -> new Vector2((float) Math.sin(v.x), (float) Math.sin(v.y));
            case COS -> new Vector2((float) Math.cos(v.x), (float) Math.cos(v.y));
            case TAN -> new Vector2((float) Math.tan(v.x), (float) Math.tan(v.y));
            case SQRT -> new Vector2((float) Math.sqrt(v.x), (float) Math.sqrt(v.y));
            case CBRT -> new Vector2((float) Math.cbrt(v.x), (float) Math.cbrt(v.y));
            case ABS -> new Vector2(Math.abs(v.x), Math.abs(v.y));
        };
    }

    @Override
    public UnaryVecProvider copy() {
        return new UnaryVecProvider(operation, value.copy());
    }
}
