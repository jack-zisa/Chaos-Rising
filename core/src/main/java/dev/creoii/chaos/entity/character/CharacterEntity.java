package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.ai.controller.CharacterController;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.util.stat.Stats;

public class CharacterEntity extends LivingEntity {
    public CharacterEntity(Vector2 collider) {
        super(collider, Group.CHARACTER, new Stats(), new Stats());
    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return new CharacterController(this);
    }

    @Override
    public void collide(LivingEntity other) {
    }
}
