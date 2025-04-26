package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class TargetPosVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        return Vector2.Zero;
    }

    @Override
    public VecProvider copy() {
        return new TargetPosVecProvider();
    }
}
