package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public class EntityRenderer implements Renderable {
    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        for (Entity entity : renderer.getMain().getGame().getEntityManager().getActiveEntities().values()) {
            entity.render(renderer, batch, shapeRenderer, font, debug);
        }
    }
}
