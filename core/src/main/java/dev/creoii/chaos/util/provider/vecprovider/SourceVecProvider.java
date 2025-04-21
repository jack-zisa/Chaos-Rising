package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class SourceVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        return context.sourceEntity().getCenterPos().cpy();
    }

    @Override
    public VecProvider copy() {
        return new SourceVecProvider();
    }
}
