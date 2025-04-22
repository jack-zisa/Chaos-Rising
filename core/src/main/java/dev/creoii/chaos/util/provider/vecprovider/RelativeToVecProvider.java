package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class RelativeToVecProvider implements VecProvider {
    private final VecProvider parent;
    private final VecProvider offset;

    public RelativeToVecProvider(VecProvider parent, VecProvider offset) {
        this.parent = parent;
        this.offset = offset;
    }

    @Override
    public Vector2 get(Context context) {
        return parent.get(context).add(offset.get(context));
    }

    @Override
    public VecProvider copy() {
        return new RelativeToVecProvider(parent.copy(), offset.copy());
    }
}
