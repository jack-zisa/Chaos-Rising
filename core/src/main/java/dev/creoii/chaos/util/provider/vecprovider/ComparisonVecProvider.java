package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public record ComparisonVecProvider(BooleanProvider comparison, VecProvider trueValue, VecProvider falseValue) implements VecProvider {
    public static final MapCodec<ComparisonVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("comparison").forGetter(ComparisonVecProvider::comparison),
            VecProvider.CODEC.fieldOf("true_value").forGetter(ComparisonVecProvider::trueValue),
            VecProvider.CODEC.fieldOf("false_value").forGetter(ComparisonVecProvider::falseValue)
        ).apply(instance, ComparisonVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.COMPARISON;
    }

    @Override
    public Vector2 get(Context context) {
        return comparison.get(context) ? trueValue.get(context) : falseValue.get(context);
    }

    @Override
    public VecProvider copy() {
        return new ComparisonVecProvider(comparison.copy(), trueValue.copy(), falseValue.copy());
    }
}
