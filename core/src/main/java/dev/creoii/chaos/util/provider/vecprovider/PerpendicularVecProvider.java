package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class PerpendicularVecProvider implements VecProvider {
    private final VecProvider value;

    public PerpendicularVecProvider(VecProvider value) {
        this.value = value;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 base = value.get(context);
        return new Vector2(-base.y, base.x).cpy();
    }

    @Override
    public VecProvider copy() {
        return new PerpendicularVecProvider(value.copy());
    }
}
