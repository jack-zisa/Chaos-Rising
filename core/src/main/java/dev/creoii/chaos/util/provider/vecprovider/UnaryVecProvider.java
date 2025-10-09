package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryVecProvider(UnaryOperation operation, VecProvider value) implements VecProvider {
    public static final MapCodec<UnaryVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            UnaryOperation.CODEC.fieldOf("operation").orElse(UnaryOperation.SIN).forGetter(UnaryVecProvider::operation),
            VecProvider.CODEC.fieldOf("value").forGetter(UnaryVecProvider::value)
        ).apply(instance, UnaryVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.UNARY;
    }

    @Override
    public Vector2 get(Context context) {
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
