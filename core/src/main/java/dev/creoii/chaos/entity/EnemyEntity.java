package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;

import java.util.UUID;

public class EnemyEntity extends LivingEntity {
    public EnemyEntity(Game game, EntityType<? extends EnemyEntity> type, UUID uuid, Vector2 pos) {
        super(game, type, uuid, pos);
    }
}
