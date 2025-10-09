package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.UnaryOperation;

public record UnaryVecProvider(UnaryOperation function, VecProvider value) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        Vector2 v = value.get(context);
        return switch (function) {
            case SIN -> new Vector2((float) Math.sin(v.x), (float) Math.sin(v.y));
            case COS -> new Vector2((float) Math.cos(v.x), (float) Math.cos(v.y));
            case TAN -> new Vector2((float) Math.tan(v.x), (float) Math.tan(v.y));
            case SQRT -> new Vector2((float) Math.sqrt(v.x), (float) Math.sqrt(v.y));
            case CBRT -> new Vector2((float) Math.cbrt(v.x), (float) Math.cbrt(v.y));
            case ABS -> new Vector2(Math.abs(v.x), Math.abs(v.y));
        };
    }

    @Override
    public UnaryVecProvider copy() {
        return new UnaryVecProvider(function, value.copy());
    }
}
