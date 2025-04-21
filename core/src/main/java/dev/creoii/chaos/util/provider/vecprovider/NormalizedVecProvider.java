package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;

public class NormalizedVecProvider implements VecProvider {
    private final VecProvider value;

    public NormalizedVecProvider(VecProvider value) {
        this.value = value;
    }

    @Override
    public Vector2 get(Context context) {
        return value.get(context).nor().cpy();
    }

    @Override
    public VecProvider copy() {
        return new NormalizedVecProvider(value.copy());
    }
}
