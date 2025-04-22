package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;

public class ParentVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        if (context.sourceEntity() instanceof BulletEntity bullet) {
            return bullet.getParent().getCenterPos();
        }
        return Vector2.Zero.cpy();
    }

    @Override
    public VecProvider copy() {
        return new ParentVecProvider();
    }
}
