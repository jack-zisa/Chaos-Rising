package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.render.data.*;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityRenderManager extends EntityManager<EntityRenderData> implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final ObjectMap<UUID, EntityRenderData> visibleEntities;
    private int size;
    private int visibleSize;

    public EntityRenderManager(ClientGame game) {
        super(game);
        visibleEntities = new ObjectMap<>(128);

        EntityRenderers.register(EntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LivingEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntityRenderData.class, BulletEntityRenderer::new);
        EntityRenderers.register(LootDropEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(CharacterEntityRenderData.class, SimpleEntityRenderer::new);
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getVisibleSize() {
        return visibleSize;
    }

    public void addEntity(UUID uuid, EntityRenderData entity) {
        visibleEntities.put(uuid, entity);
        ++size;
        EntityRenderers.getRenderer(entity).init(this);
    }

    @Nullable
    public EntityRenderData getEntityData(UUID uuid) {
        return visibleEntities.get(uuid);
    }

    @Override
    public boolean removeEntity(UUID uuid) {
        visibleEntities.remove(uuid);
        --size;
        return true;
    }

    public static Sprite getSprite(ClientGame game, EntityRenderData entity) {
        Sprite sprite = new Sprite(game.getTextureManager().getTexture(entity.group.name().toLowerCase(), entity.textureId));
        sprite.setSize(entity.scale, entity.scale);
        return sprite;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        visibleSize = 0;
        float camX = renderer.getCamera().position.x - renderer.getCamera().viewportWidth / 2;
        float camY = renderer.getCamera().position.y - renderer.getCamera().viewportHeight / 2;
        float camW = renderer.getCamera().viewportWidth;
        float camH = renderer.getCamera().viewportHeight;
        for (EntityRenderData entity : visibleEntities.values()) {
            if (isEntityInView(renderer.getCamera().position, camX, camY, camW, camH, entity)) {
                ++visibleSize;
                EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, debug);
            }
        }
    }

    private boolean isEntityInView(Vector3 cameraPos, float camX, float camY, float camW, float camH, EntityRenderData entity) {
        if (entity == null)
            return false;

        float xd = cameraPos.x - entity.x;
        float yd = cameraPos.y - entity.y;

        if (xd * xd + yd * yd > RENDER_DISTANCE) {
            return false;
        }
        Sprite sprite = getSprite((ClientGame) getGame(), entity);
        return camX < entity.x + sprite.getWidth() && camX + camW > entity.x && camY < entity.y + sprite.getHeight() && camY + camH > entity.y;
    }
}
