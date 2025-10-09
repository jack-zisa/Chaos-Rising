package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public record ComparisonNumberProvider(BooleanProvider comparison, NumberProvider trueValue, NumberProvider falseValue) implements NumberProvider {
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
