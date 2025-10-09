package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record RotateAngleVecProvider(VecProvider direction, NumberProvider angle) implements VecProvider {

    @Override
    public Vector2 get(Context context) {
        Vector2 direction = this.direction.get(context);

        if (direction.len() != 1f)
            direction.nor();

        return direction.rotateRad(angle.get(context));
    }

    @Override
    public VecProvider copy() {
        return new RotateAngleVecProvider(direction.copy(), angle.copy());
    }
}
