package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public record ComparisonColorProvider(BooleanProvider comparison, ColorProvider trueValue, ColorProvider falseValue) implements ColorProvider {
    public static final MapCodec<ComparisonColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("comparison").forGetter(ComparisonColorProvider::comparison),
            ColorProvider.CODEC.fieldOf("true_value").forGetter(ComparisonColorProvider::trueValue),
            ColorProvider.CODEC.fieldOf("false_value").forGetter(ComparisonColorProvider::falseValue)
        ).apply(instance, ComparisonColorProvider::new);
    });

    @Override
    public Type getType() {
        return Type.COMPARISON;
    }

    @Override
    public Color get(Context context) {
        return comparison.get(context) ? trueValue.get(context) : falseValue.get(context);
    }
}
