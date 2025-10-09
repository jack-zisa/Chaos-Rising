package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import javax.annotation.Nullable;

public record ClampVecProvider(VecProvider vec, @Nullable NumberProvider minX, @Nullable NumberProvider minY, @Nullable NumberProvider maxX, @Nullable NumberProvider maxY) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        Vector2 v = vec.get(context);

        float x = v.x;
        float y = v.y;

        if (minX != null)
            x = Math.max(x, minX.get(context));
        if (maxX != null)
            x = Math.min(x, maxX.get(context));
        if (minY != null)
            y = Math.max(y, minY.get(context));
        if (maxY != null)
            y = Math.min(y, maxY.get(context));

        return new Vector2(x, y);
    }

    @Override
    public VecProvider copy() {
        return new ClampVecProvider(vec.copy(), minX != null ? minX.copy() : null, minY != null ? minY.copy() : null, maxX != null ? maxX.copy() : null, maxY != null ? maxY.copy() : null);
    }
}
