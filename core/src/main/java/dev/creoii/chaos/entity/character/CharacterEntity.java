package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.controller.EntityController;

import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private final EntityController<CharacterEntity> controller;

    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.spritePath(), 1f, new Vector2(1, 1), Group.CHARACTER, characterClass.baseStats().copy(), characterClass.baseStats().copy());
        controller = new CharacterController(this);
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        sprite = new Sprite(game.getTextureManager().getTexture("class", getTextureId()));
        sprite.setSize(getScale(), getScale());
        return this;
    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return controller;
    }

    @Override
    public void collide(Entity other) {
    }
}
