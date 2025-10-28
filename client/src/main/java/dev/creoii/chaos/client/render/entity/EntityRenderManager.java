package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.util.Renderable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.event.SpawnEntityEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import javax.annotation.Nullable;

public class EntityRenderManager extends EntityManager<EntityRenderData> implements Renderable {
    private static final float RENDER_DISTANCE = 17578.125f * Entity.COORDINATE_SCALE; // sqrt(17578.125 * 32) = 750 units
    private final ObjectList<EntityRenderData> visibleEntities = new ObjectArrayList<>();
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

    public void addEntity(int id, EntityRenderData entity) {
        getEntities(entity.group).put(id, entity);
        setSize(getSize() + 1);

        EntityRenderers.getRenderer(entity).init(this, entity);
        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), id);
    }

    @Nullable
    public EntityRenderData getEntityData(EntityGroup group, int id) {
        return getEntities(group).get(id);
    }

    @Nullable
    public EntityRenderData getEntityData(int id) {
        for (EntityGroup group : EntityGroup.values()) {
            EntityRenderData entityData = getEntityData(group, id);
            if (entityData != null)
                return entityData;
        }
        return null;
    }

    public boolean removeEntity(EntityGroup group, int id) {
        return removeEntity(group, id, false);
    }

    public boolean removeEntity(EntityGroup group, int id, boolean remove) {
        if (remove)
            setSize(getSize() - 1);
        return getEntities(group).remove(id) != null;
    }

    public boolean removeEntity(int id) {
        for (EntityGroup group : EntityGroup.values()) {
            if (removeEntity(group, id)) {
                setSize(getSize() - 1);
                return true;
            }
        }
        return false;
    }

    public static Sprite getSprite(ClientGame game, EntityRenderData entity) {
        Sprite sprite = new Sprite(game.getAssetManager().getTextureManager().getTexture(TextureManager.AtlasKey.fromEntityGroup(entity.group), entity.textureId));
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

        visibleEntities.clear();

        for (Int2ObjectOpenHashMap<EntityRenderData> map : getAllEntities().values()) {
            for (Int2ObjectMap.Entry<EntityRenderData> entry : map.int2ObjectEntrySet()) {
                if (isEntityInView(renderer.getCamera().position, camX, camY, camW, camH, entry.getValue())) {
                    visibleEntities.add(entry.getValue());
                }
            }
        }
        visibleEntities.sort((a, b) -> Float.compare(b.renderY, a.renderY));

        for (EntityRenderData entity : visibleEntities) {
            ++visibleSize;
            EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, delta, debug);
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
