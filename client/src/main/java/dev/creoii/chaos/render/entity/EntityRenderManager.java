package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
    private final Array<Entity> visibleEntities;

    public EntityRenderManager(Main main) {
        this.main = main;
        EntityRenderers.register(CharacterEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LootDropEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(EnemyEntity.class, EnemyEntityRenderer::new);
        visibleEntities = new Array<>(128);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (debug && shapeRenderer != null) {
            renderCollisionGrid(shapeRenderer);
        }

        refreshVisibleEntities(renderer);
        Array<Entity> visibleEntities = this.visibleEntities;
        visibleEntities.sort((a, b) -> Float.compare(b.getPos().y, a.getPos().y));

        for (Entity entity : visibleEntities) {
            EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, debug);
        }
    }

    private void renderCollisionGrid(ShapeRenderer shapeRenderer) {
        /*for (ObjectMap.Entry<Integer, Array<Entity>> entry : main.getGame().getCollisionManager().getGrid().entries()) {
            int x = (entry.key >>> 16) - CollisionManager.KEY_OFFSET;
            int y = (entry.key & 0xffff) - CollisionManager.KEY_OFFSET;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.FIREBRICK);
            float cellSize = main.getGame().getCollisionManager().getCellSize();
            shapeRenderer.rect(x * cellSize, y * cellSize, cellSize, cellSize);
            shapeRenderer.end();
        }*/
    }

    private void refreshVisibleEntities(Renderer renderer) {
        visibleEntities.clear();
        float camX = renderer.getCamera().position.x - renderer.getCamera().viewportWidth / 2;
        float camY = renderer.getCamera().position.y - renderer.getCamera().viewportHeight / 2;
        float camW = renderer.getCamera().viewportWidth;
        float camH = renderer.getCamera().viewportHeight;
        /*for (Entity entity : renderer.getMain().getGame().getEntityManager().getEntities().values()) {
            if (entity == renderer.getMain().getGame().getCharacter() || isEntityInView(renderer.getCamera().position, camX, camY, camW, camH, entity)) {
                visibleEntities.add(entity);
            }
        }*/
    }

    private boolean isEntityInView(Vector3 cameraPos, float camX, float camY, float camW, float camH, Entity entity) {
        if (entity.getCenterPos().dst2(cameraPos.x, cameraPos.y) > RENDER_DISTANCE) {
            return false;
        }
        return camX < entity.getColliderRect().x + entity.getColliderRect().width && camX + camW > entity.getColliderRect().x && camY < entity.getColliderRect().y + entity.getColliderRect().height && camY + camH > entity.getColliderRect().y;
    }
}
