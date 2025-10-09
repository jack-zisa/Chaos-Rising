package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public record ComparisonNumberProvider(BooleanProvider comparison, NumberProvider trueValue, NumberProvider falseValue) implements NumberProvider {
    public static final MapCodec<ComparisonNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("comparison").forGetter(ComparisonNumberProvider::comparison),
            NumberProvider.CODEC.fieldOf("trueValue").forGetter(ComparisonNumberProvider::trueValue),
            NumberProvider.CODEC.fieldOf("falseValue").forGetter(ComparisonNumberProvider::falseValue)
        ).apply(instance, ComparisonNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.COMPARISON;
    }

    @Override
    public Float get(Context context) {
        return comparison.get(context) ? trueValue.get(context) : falseValue.get(context);
    }

    @Override
    public NumberProvider copy() {
        return new ComparisonNumberProvider(comparison.copy(), trueValue.copy(), falseValue.copy());
    }

    @Override
    public NumberProvider init(int startTime) {
        comparison.init(startTime);
        trueValue.init(startTime);
        falseValue.init(startTime);
        return this;
    }
}
