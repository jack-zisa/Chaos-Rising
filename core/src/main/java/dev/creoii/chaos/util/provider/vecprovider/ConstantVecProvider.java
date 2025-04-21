package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.floatprovider.ConstantFloatProvider;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;

public class ConstantVecProvider implements VecProvider {
    private final FloatProvider x, y;

    public ConstantVecProvider(FloatProvider x, FloatProvider y) {
        this.x = x;
        this.y = y;
    }

    public ConstantVecProvider(Vector2 vector2) {
        this.x = new ConstantFloatProvider(vector2.x);
        this.y = new ConstantFloatProvider(vector2.y);
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

