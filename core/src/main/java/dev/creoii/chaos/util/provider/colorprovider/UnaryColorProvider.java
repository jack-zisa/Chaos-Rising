package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryColorProvider(UnaryOperation operation, ColorProvider value) implements ColorProvider {
    public static final MapCodec<UnaryColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            UnaryOperation.CODEC.fieldOf("operation").orElse(UnaryOperation.SIN).forGetter(UnaryColorProvider::operation),
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, UnaryColorProvider::new);
    });
    public static final MapCodec<UnaryColorProvider> SIN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.SIN, value));
    });
    public static final MapCodec<UnaryColorProvider> COS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.COS, value));
    });
    public static final MapCodec<UnaryColorProvider> TAN_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.TAN, value));
    });
    public static final MapCodec<UnaryColorProvider> SQRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.SQRT, value));
    });
    public static final MapCodec<UnaryColorProvider> CBRT_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.CBRT, value));
    });
    public static final MapCodec<UnaryColorProvider> ABS_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            ColorProvider.CODEC.fieldOf("value").forGetter(UnaryColorProvider::value)
        ).apply(instance, value -> new UnaryColorProvider(UnaryOperation.ABS, value));
    });

    @Override
    public Type getType() {
        return Type.UNARY;
    }

    @Override
    public Color get(Context context) {
        Color v = value.get(context);
        return switch (operation) {
            case SIN -> sin(v);
            case COS -> cos(v);
            case TAN -> tan(v);
            case SQRT -> sqrt(v);
            case CBRT -> cbrt(v);
            case ABS -> abs(v);
        };
    }

    @Override
    public Provider<Color> optimize() {
        if (value instanceof ConstantColorProvider(Color color)) {
            return new ConstantColorProvider(switch (operation) {
                case SIN -> sin(color);
                case COS -> cos(color);
                case TAN -> tan(color);
                case SQRT -> sqrt(color);
                case CBRT -> cbrt(color);
                case ABS -> abs(color);
            });
        }
        return ColorProvider.super.optimize();
    }

    public static Color sin(Color color) {
        color.r = (float) Math.sin(color.r);
        color.g = (float) Math.sin(color.g);
        color.b = (float) Math.sin(color.b);
        color.a = (float) Math.sin(color.a);
        return color;
    }

    public static Color cos(Color color) {
        color.r = (float) Math.cos(color.r);
        color.g = (float) Math.cos(color.g);
        color.b = (float) Math.cos(color.b);
        color.a = (float) Math.cos(color.a);
        return color;
    }

    public static Color tan(Color color) {
        color.r = (float) Math.tan(color.r);
        color.g = (float) Math.tan(color.g);
        color.b = (float) Math.tan(color.b);
        color.a = (float) Math.tan(color.a);
        return color;
    }

    public static Color sqrt(Color color) {
        color.r = (float) Math.sqrt(color.r);
        color.g = (float) Math.sqrt(color.g);
        color.b = (float) Math.sqrt(color.b);
        color.a = (float) Math.sqrt(color.a);
        return color;
    }

    public static Color cbrt(Color color) {
        color.r = (float) Math.cbrt(color.r);
        color.g = (float) Math.cbrt(color.g);
        color.b = (float) Math.cbrt(color.b);
        color.a = (float) Math.cbrt(color.a);
        return color;
    }

    public static Color abs(Color color) {
        color.r = Math.abs(color.r);
        color.g = Math.abs(color.g);
        color.b = Math.abs(color.b);
        color.a = Math.abs(color.a);
        return color;
    }
}
