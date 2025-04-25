package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.EnemyEntity;

public class SpawnPosVecProvider implements VecProvider {
    @Override
    public Vector2 get(Context context) {
        if (context.sourceEntity() instanceof EnemyEntity enemy) {
            return enemy.getSpawnPos();
        }
        return Vector2.Zero.cpy();
    }

    @Override
    public VecProvider copy() {
        return new SpawnPosVecProvider();
    }
}
