package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class RotatedOffsetVecProvider implements VecProvider {
    private final VecProvider from;
    private final VecProvider to;
    private final VecProvider offset;

    public RotatedOffsetVecProvider(VecProvider from, VecProvider to, VecProvider offset) {
        this.from = from;
        this.to = to;
        this.offset = offset;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 direction = to.get(context).sub(from.get(context));
        return offset.get(context).rotateRad(direction.angleRad());
    }

    @Override
    public VecProvider copy() {
        return new RotatedOffsetVecProvider(from.copy(), to.copy(), offset.copy());
    }
}
