package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.booleanprovider.ConstantBooleanProvider;

public record ComparisonNumberProvider(BooleanProvider comparison, NumberProvider trueValue, NumberProvider falseValue) implements NumberProvider {
    public static final MapCodec<ComparisonNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("comparison").forGetter(ComparisonNumberProvider::comparison),
            NumberProvider.CODEC.fieldOf("true_value").orElse(ConstantNumberProvider.ZERO).forGetter(ComparisonNumberProvider::trueValue),
            NumberProvider.CODEC.fieldOf("false_value").orElse(ConstantNumberProvider.ZERO).forGetter(ComparisonNumberProvider::falseValue)
        ).apply(instance, ComparisonNumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (comparison instanceof ConstantBooleanProvider(boolean value3) && trueValue instanceof ConstantNumberProvider(float value1) && falseValue instanceof ConstantNumberProvider(float value2)) {
            return new ConstantNumberProvider(value3 ? value1 : value2);
        }
        return NumberProvider.super.optimize();
    }

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
