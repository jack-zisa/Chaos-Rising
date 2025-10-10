package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.util.Renderable;

import javax.annotation.Nullable;

public class EntityRenderManager extends EntityManager<EntityRenderData> implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final ObjectMap<Integer, EntityRenderData> visibleEntities = new ObjectMap<>(256);
    private int visibleSize;

    public EntityRenderManager(ClientGame game) {
        super(game);
        EntityRenderers.register(EntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LivingEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntityRenderData.class, BulletEntityRenderer::new);
        EntityRenderers.register(LootDropEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(CharacterEntityRenderData.class, SimpleEntityRenderer::new);
    }

    public int getVisibleSize() {
        return visibleSize;
    }

    public EntityRenderData addEntity(int id, EntityRenderData entity) {
        setSize(getSize() + 1);
        EntityRenderers.getRenderer(entity).init(this);
        return visibleEntities.put(id, entity);
    }

    @Nullable
    public EntityRenderData getEntityData(int id) {
        return visibleEntities.get(id);
    }

    @Override
    public boolean removeEntity(int id) {
        visibleEntities.remove(id);
        setSize(getSize() - 1);
        return super.removeEntity(id);
    }

    public static Sprite getSprite(ClientGame game, EntityRenderData entity) {
        Sprite sprite = new Sprite(game.getAssetManager().getTextureManager().getTexture(entity.group.name().toLowerCase(), entity.textureId));
        sprite.setSize(entity.scale, entity.scale);
        return sprite;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        visibleSize = 0;
        float camX = renderer.getCamera().position.x - renderer.getCamera().viewportWidth / 2;
        float camY = renderer.getCamera().position.y - renderer.getCamera().viewportHeight / 2;
        float camW = renderer.getCamera().viewportWidth;
        float camH = renderer.getCamera().viewportHeight;
        for (EntityRenderData entity : visibleEntities.values()) {
            if (isEntityInView(renderer.getCamera().position, camX, camY, camW, camH, entity)) {
                ++visibleSize;
                EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, delta, debug);
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
