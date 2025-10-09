package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

public record ComparisonVecProvider(BooleanProvider comparison, VecProvider trueValue, VecProvider falseValue) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        return comparison.get(context) ? trueValue.get(context) : falseValue.get(context);
    }

    @Override
    public VecProvider copy() {
        return new ComparisonVecProvider(comparison.copy(), trueValue.copy(), falseValue.copy());
    }
}
