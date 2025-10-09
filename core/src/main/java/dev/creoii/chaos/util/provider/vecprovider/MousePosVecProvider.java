package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;

public record MousePosVecProvider() implements VecProvider {
    private static final MousePosVecProvider INSTANCE = new MousePosVecProvider();
    public static final MapCodec<MousePosVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.TARGET_POS;
    }

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
