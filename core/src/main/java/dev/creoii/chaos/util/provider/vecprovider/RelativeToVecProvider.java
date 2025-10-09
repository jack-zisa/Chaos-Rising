package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public record RelativeToVecProvider(VecProvider parent, VecProvider offset) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        return parent.get(context).add(offset.get(context));
    }

    @Override
    public VecProvider copy() {
        return new RelativeToVecProvider(parent.copy(), offset.copy());
    }
}
