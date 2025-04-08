package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public class EntityRenderer implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        for (Entity entity : renderer.getMain().getGame().getEntityManager().getActiveEntities().values()) {
            if (entity == renderer.getMain().getGame().getActiveCharacter() || isEntityInView(renderer.getCamera(), entity)) {
                entity.render(renderer, batch, shapeRenderer, font, debug);
            }
        }
    }

    public boolean isEntityInView(OrthographicCamera camera, Entity entity) {
        if (entity.getCenterPos().dst2(camera.position.x, camera.position.y) > RENDER_DISTANCE) {
            return false;
        }
        float camX = camera.position.x - camera.viewportWidth / 2;
        float camY = camera.position.y - camera.viewportHeight / 2;
        return new Rectangle(camX, camY, camera.viewportWidth, camera.viewportHeight).overlaps(entity.getColliderRect());
    }
}
