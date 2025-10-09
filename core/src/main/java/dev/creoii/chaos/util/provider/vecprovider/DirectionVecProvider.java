package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public record DirectionVecProvider(VecProvider from, VecProvider to) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        return to.get(context).sub(from.get(context));
    }

    @Override
    public VecProvider copy() {
        return new DirectionVecProvider(from.copy(), to.copy());
    }
}
