package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.CollisionManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public class EntityRenderManager implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final Main main;

    public EntityRenderManager(Main main) {
        this.main = main;
        EntityRenderers.register(CharacterEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LootDropEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(EnemyEntity.class, EnemyEntityRenderer::new);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (debug && shapeRenderer != null) {
            for (ObjectMap.Entry<Integer, Array<Entity>> entry : main.getGame().getCollisionManager().getGrid().entries()) {
                int x = (entry.key >> 16) - CollisionManager.KEY_OFFSET;
                int y = (entry.key & 0xffff) - CollisionManager.KEY_OFFSET;
                shapeRenderer.setColor(Color.FIREBRICK);
                float cellSize = main.getGame().getCollisionManager().getCellSize();
                shapeRenderer.rect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        Array<Entity> visibleEntities = new Array<>();
        for (Entity entity : renderer.getMain().getGame().getEntityManager().getActiveEntities().values()) {
            if (entity == renderer.getMain().getGame().getActiveCharacter() || isEntityInView(renderer.getCamera(), entity)) {
                visibleEntities.add(entity);
            }
        }

        visibleEntities.sort((a, b) -> Float.compare(b.getPos().y, a.getPos().y));

        for (Entity entity : visibleEntities) {
            EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, debug);
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
