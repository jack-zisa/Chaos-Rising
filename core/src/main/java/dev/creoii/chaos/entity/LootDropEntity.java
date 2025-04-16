package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;

import java.util.UUID;

public class LootDropEntity extends Entity {
    public LootDropEntity(String textureId, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.OTHER);
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        LootDropEntity entity = new LootDropEntity(getTextureId(), 1f);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("loot", getTextureId()));
        entity.sprite.setSize(getScale(), getScale());
        return entity;
    }

    @Override
    public EntityController<?> getController() {
        return null;
    }

    @Override
    public void collide(Entity other) {
    }

    @Override
    public void postSpawn() {
    }
}
