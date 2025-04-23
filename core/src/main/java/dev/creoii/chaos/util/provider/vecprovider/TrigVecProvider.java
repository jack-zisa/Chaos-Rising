package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.TrigFunction;

public class TrigVecProvider implements VecProvider {
    private final TrigFunction function;
    private final VecProvider value;

    public TrigVecProvider(TrigFunction function, VecProvider value) {
        this.function = function;
        this.value = value;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 v = value.get(context);
        return switch (function) {
            case SIN -> new Vector2((float) Math.sin(v.x), (float) Math.sin(v.y));
            case COS -> new Vector2((float) Math.cos(v.x), (float) Math.cos(v.y));
            case TAN -> new Vector2((float) Math.tan(v.x), (float) Math.tan(v.y));
        };
    }

    @Override
    public TrigVecProvider copy() {
        return new TrigVecProvider(function, value.copy());
    }
}
