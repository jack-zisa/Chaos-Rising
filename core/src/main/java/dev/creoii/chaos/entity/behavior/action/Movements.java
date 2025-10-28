package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

public class Movements {
    public static final Map<String, BiConsumer<Entity, Float>> MOVEMENTS = Map.of(
        "random", Movements::random,
        "chase", Movements::chase
    );

    public static void random(Entity entity, float dt) {
        float speed = (entity instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
        entity.getPos().x += (Math.random() < .5f ? -1f : 1f) * speed * dt;
        entity.getPos().y += (Math.random() < .5f ? -1f : 1f) * speed * dt;
    }

    public static void chase(Entity entity, float dt) {
        CharacterEntity character = getNearestCharacter(entity);
        if (character != null) {
            float speed = (entity instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
            Vector2 direction = new Vector2(character.getPos()).sub(entity.getPos()).nor();
            entity.getPos().add(direction.nor().scl(speed * dt));
        }
    }

    @Nullable
    public static CharacterEntity getNearestCharacter(Entity entity) {
        Game game = entity.getGame();
        if (!game.isClient()) {
            EntityManager<?> entityManager = game.getEntityManager();
            return entityManager.getEntities(EntityGroup.CHARACTER).values().stream()
                .map(o -> (CharacterEntity) o)
                .min(Comparator.comparingDouble(c -> entity.getPos().dst2(c.getPos())))
                .orElse(null);
        }
        return null;
    }
}
