package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public class ConstantVecProvider implements VecProvider {
    private final NumberProvider x, y;

    public ConstantVecProvider(NumberProvider x, NumberProvider y) {
        this.x = x;
        this.y = y;
    }

    public ConstantVecProvider(Vector2 vector2) {
        this.x = new ConstantNumberProvider(vector2.x);
        this.y = new ConstantNumberProvider(vector2.y);
    }

    @Override
    public Vector2 get(Context context) {
        return new Vector2(x.get(context), y.get(context));
    }

    @Override
    public VecProvider copy() {
        return new ConstantVecProvider(x.copy(), y.copy());
    }
}

