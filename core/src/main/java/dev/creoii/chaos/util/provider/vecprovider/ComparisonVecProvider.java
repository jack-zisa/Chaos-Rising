package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public class ComparisonVecProvider implements VecProvider {
    private final BooleanProvider comparison;
    private final VecProvider trueValue;
    private final VecProvider falseValue;

    public ComparisonVecProvider(BooleanProvider booleanProvider, VecProvider trueValue, VecProvider falseValue) {
        this.comparison = booleanProvider;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
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
