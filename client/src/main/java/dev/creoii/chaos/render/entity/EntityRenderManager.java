package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityRenderManager implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final ClientGame game;
    private final ObjectMap<UUID, Entity> visibleEntities;

    public EntityRenderManager(ClientGame game) {
        this.game = game;
        visibleEntities = new ObjectMap<>(128);

        EntityRenderers.register(CharacterEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntity.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LootDropEntity.class, SimpleEntityRenderer::new);
    }

    public ClientGame getGame() {
        return game;
    }

    public void addEntity(Entity entity) {
        visibleEntities.put(entity.getUuid(), entity);
    }

    public Entity getEntity(UUID uuid) {
        return visibleEntities.get(uuid);
    }

    public void removeEntity(UUID uuid) {
        visibleEntities.remove(uuid);
    }

    public void updateEntity(UUID uuid, float x, float y) {
        getEntity(uuid).setPos(x, y);
    }

    public static Sprite getSprite(ClientGame game, Entity entity) {
        return new Sprite(game.getTextureManager().getTexture("entity", entity.getType().id()));
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
        for (Entity entity : visibleEntities.values()) {
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

    private boolean isEntityInView(Vector3 cameraPos, float camX, float camY, float camW, float camH, Entity entity) {
        if (entity.getPos().dst2(cameraPos.x, cameraPos.y) > RENDER_DISTANCE) {
            return false;
        }
        Sprite sprite = getSprite(game, entity);
        return camX < entity.getPos().x + sprite.getWidth() && camX + camW > entity.getPos().x && camY < entity.getPos().y + sprite.getHeight() && camY + camH > entity.getPos().y;
    }
}
