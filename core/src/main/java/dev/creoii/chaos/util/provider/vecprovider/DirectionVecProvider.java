package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class DirectionVecProvider implements VecProvider {
    private final VecProvider from;
    private final VecProvider to;

    public DirectionVecProvider(VecProvider from, VecProvider to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Vector2 get(Context context) {
        return to.get(context).sub(from.get(context));
    }

    @Override
    public VecProvider copy() {
        return new DirectionVecProvider(from.copy(), to.copy());
    }
}
