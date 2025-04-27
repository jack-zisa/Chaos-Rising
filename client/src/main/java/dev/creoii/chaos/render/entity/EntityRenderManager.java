package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.ClientEntity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityRenderManager implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * ClientEntity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final ObjectMap<UUID, ClientEntity> visibleEntities;

    public EntityRenderManager() {
        visibleEntities = new ObjectMap<>(128);

        EntityRenderers.register(CharacterEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LootDropEntity.class, SimpleEntityRenderer::new);
    }

    public void addEntity(ClientEntity entity) {
        visibleEntities.put(entity.getUuid(), entity);
    }

    public ClientEntity getEntity(UUID uuid) {
        return visibleEntities.get(uuid);
    }

    public void removeEntity(UUID uuid) {
        visibleEntities.remove(uuid);
    }

    public void updateEntity(UUID uuid, float x, float y) {
        getEntity(uuid).setPos(x, y);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (debug && shapeRenderer != null) {
            renderCollisionGrid(shapeRenderer);
        }

        float camX = renderer.getCamera().position.x - renderer.getCamera().viewportWidth / 2;
        float camY = renderer.getCamera().position.y - renderer.getCamera().viewportHeight / 2;
        float camW = renderer.getCamera().viewportWidth;
        float camH = renderer.getCamera().viewportHeight;
        for (ClientEntity entity : visibleEntities.values()) {
            if (isEntityInView(renderer.getCamera().position, camX, camY, camW, camH, entity)) {
                EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, debug);
            }
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

    private boolean isEntityInView(Vector3 cameraPos, float camX, float camY, float camW, float camH, ClientEntity entity) {
        if (entity.getPos().dst2(cameraPos.x, cameraPos.y) > RENDER_DISTANCE) {
            return false;
        }
        return camX < entity.getPos().x + entity.getSprite().getWidth() && camX + camW > entity.getPos().x && camY < entity.getPos().y + entity.getSprite().getHeight() && camY + camH > entity.getPos().y;
    }
}
