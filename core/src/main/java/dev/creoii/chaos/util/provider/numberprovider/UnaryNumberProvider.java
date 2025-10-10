package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryNumberProvider(UnaryOperation operation, NumberProvider value) implements NumberProvider {
    public static final MapCodec<UnaryNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            UnaryOperation.CODEC.fieldOf("operation").orElse(UnaryOperation.SIN).forGetter(UnaryNumberProvider::operation),
            NumberProvider.CODEC.fieldOf("value").forGetter(UnaryNumberProvider::value)
        ).apply(instance, UnaryNumberProvider::new);
    });

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
