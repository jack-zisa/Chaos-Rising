package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.render.Renderer;

import javax.annotation.Nullable;

public class EnemyEntityRenderer extends SimpleEntityRenderer<EnemyEntity> {
    public EnemyEntityRenderer(Entity entity) {
        super((EnemyEntity) entity);
    }

    public void render(EnemyEntity entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        super.render(entity, renderer, batch, shapeRenderer, font, debug);

        if (debug && batch != null) {
            font.draw(batch, entity.getStats().health + "/" + entity.getMaxStats().health, entity.getPos().x, entity.getPos().y);
        }
    }
}
