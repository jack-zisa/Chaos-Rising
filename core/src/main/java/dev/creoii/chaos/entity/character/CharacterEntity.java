package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.ai.controller.CharacterController;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.stat.Stats;

import javax.annotation.Nullable;
import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.spritePath(), 1f, new Vector2(1, 1), Group.CHARACTER, new Stats(100, 5, 1, 4, 5, 5), new Stats(100, 10, 1, 10, 10, 10));
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        sprite = new Sprite(game.getTextureManager().getTexture("class", getTextureId()));
        sprite.setSize(getScale(), getScale());
        return this;
    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return new CharacterController(this);
    }

    @Override
    public void collide(LivingEntity other) {
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        super.render(renderer, batch, shapeRenderer, font, debug);

        if (shapeRenderer != null && Gdx.input.isTouched()) {
            Vector2 centerPos = getCenterPos();
            shapeRenderer.line(centerPos.x, centerPos.y, getGame().getInputManager().getMousePos().x, getGame().getInputManager().getMousePos().y);
        }
    }
}
