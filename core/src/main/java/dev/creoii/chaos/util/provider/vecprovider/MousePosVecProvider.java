package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class MousePosVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        return Vector2.Zero;
/*
        Vector3 mousePos = context.game().getInputManager().getMousePos();
        return new Vector2(mousePos.x - (ServerEntity.COORDINATE_SCALE / 2f), mousePos.y - (ServerEntity.COORDINATE_SCALE / 2f));
*/
    }

    @Override
    public VecProvider copy() {
        return new MousePosVecProvider();
    }
}
