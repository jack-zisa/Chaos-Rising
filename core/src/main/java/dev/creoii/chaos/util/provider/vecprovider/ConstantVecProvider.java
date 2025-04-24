package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import javax.annotation.Nullable;
import java.util.Objects;

public class ConstantVecProvider implements VecProvider {
    private final NumberProvider x, y;

    public ConstantVecProvider(NumberProvider x, @Nullable NumberProvider y) {
        this.x = x;
        this.y = y;
    }

    public ConstantVecProvider(Vector2 vector2) {
        this.x = new ConstantNumberProvider(vector2.x);
        this.y = new ConstantNumberProvider(vector2.y);
    }

    public ConstantVecProvider(int x, int y) {
        this.x = new ConstantNumberProvider(x);
        this.y = new ConstantNumberProvider(y);
    }

    public ConstantVecProvider(int num) {
        this(num, num);
    }

    @Override
    public Vector2 get(Context context) {
        return new Vector2(x.get(context), Objects.requireNonNullElse(y, x).get(context));
    }

    @Override
    public VecProvider copy() {
        return new ConstantVecProvider(x.copy(), y == null ? null : y.copy());
    }
}

