package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public class ComparisonNumberProvider implements NumberProvider {
    private final BooleanProvider comparison;
    private final NumberProvider trueValue;
    private final NumberProvider falseValue;

    public ComparisonNumberProvider(BooleanProvider comparison, NumberProvider trueValue, NumberProvider falseValue) {
        this.comparison = comparison;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
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
