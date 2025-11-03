package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryNumberProvider(UnaryOperation operation, NumberProvider value) implements NumberProvider {
    public static final MapCodec<UnaryNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            UnaryOperation.CODEC.fieldOf("operation").orElse(UnaryOperation.SIN).forGetter(UnaryNumberProvider::operation),
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, UnaryNumberProvider::new);
    });
    public static final MapCodec<UnaryNumberProvider> SIN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.SIN, value));
    });
    public static final MapCodec<UnaryNumberProvider> COS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.COS, value));
    });
    public static final MapCodec<UnaryNumberProvider> TAN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.TAN, value));
    });
    public static final MapCodec<UnaryNumberProvider> SQRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.SQRT, value));
    });
    public static final MapCodec<UnaryNumberProvider> CBRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.CBRT, value));
    });
    public static final MapCodec<UnaryNumberProvider> ABS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, value -> new UnaryNumberProvider(UnaryOperation.ABS, value));
    });

    @Override
    public Provider<Float> optimize() {
        if (value instanceof ConstantNumberProvider(float value1)) {
            return new ConstantNumberProvider(switch (operation) {
                case SIN -> (float) Math.sin(value1);
                case COS -> (float) Math.cos(value1);
                case TAN -> (float) Math.tan(value1);
                case SQRT -> (float) Math.sqrt(value1);
                case CBRT -> (float) Math.cbrt(value1);
                case ABS -> Math.abs(value1);
            });
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.UNARY;
    }

    @Override
    public Float get(Context context) {
        float v = value.get(context);
        return switch (operation) {
            case SIN -> (float) Math.sin(v);
            case COS -> (float) Math.cos(v);
            case TAN -> (float) Math.tan(v);
            case SQRT -> (float) Math.sqrt(v);
            case CBRT -> (float) Math.cbrt(v);
            case ABS -> Math.abs(v);
        };
    }

    @Override
    public UnaryNumberProvider copy() {
        return new UnaryNumberProvider(operation, value.copy());
    }

    @Override
    public UnaryNumberProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
