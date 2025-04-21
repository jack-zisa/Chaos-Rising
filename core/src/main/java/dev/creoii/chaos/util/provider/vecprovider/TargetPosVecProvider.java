package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class TargetPosVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        return context.game().getActiveCharacter().getCenterPos().cpy();
    }

    @Override
    public VecProvider copy() {
        return new TargetPosVecProvider();
    }
}
