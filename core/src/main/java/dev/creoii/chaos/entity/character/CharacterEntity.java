package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.ai.controller.CharacterController;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.util.stat.Stats;

import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.spritePath(), 1f, new Vector2(1, 1), Group.CHARACTER, new Stats(100, 5, 1, 4, 5, 5), new Stats(100, 10, 1, 10, 10, 10));
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        return this;
    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return new CharacterController(this);
    }

    @Override
    public void collide(LivingEntity other) {
    }
}
