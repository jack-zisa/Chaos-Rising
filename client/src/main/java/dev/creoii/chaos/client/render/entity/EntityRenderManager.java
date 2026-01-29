package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.client.ClientWorld;
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
    private static final ShaderProgram BORDER_SHADER = new ShaderProgram(Gdx.files.internal("shaders/border.vert"), Gdx.files.internal("shaders/border.frag"));
    public static final float BORDER_SIZE_MOD = 2f;

    public EntityRenderManager(ClientWorld world) {
        super(world);
        EntityRenderers.register(EntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(LivingEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(BulletEntityRenderData.class, BulletEntityRenderer::new);
        EntityRenderers.register(LootDropEntityRenderData.class, SimpleEntityRenderer::new);
        EntityRenderers.register(CharacterEntityRenderData.class, CharacterEntityRenderer::new);
    }

    public int getVisibleSize() {
        return visibleSize;
    }

    public void addEntity(int id, EntityRenderData entity) {
        getEntities(entity.group).put(id, entity);
        setSize(getSize() + 1);

        EntityRenderers.getRenderer(entity).init(this, entity);
        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), id);
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
        Sprite sprite = new Sprite(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.fromEntityGroup(entity.group), entity.textureId));
        sprite.setSize(entity.scale, entity.scale);
        return sprite;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        visibleSize = 0;
        visibleEntities.clear();

        if (batch != null) {
            batch.setShader(BORDER_SHADER);
            float border = (1f / 8f) * ((float) Math.pow(renderer.getZoom(), .85f)) / BORDER_SIZE_MOD;
            BORDER_SHADER.setUniformf("u_pixelSize", border, border);
            BORDER_SHADER.setUniformf("u_borderColor", Color.BLACK);
        }

        for (Int2ObjectOpenHashMap<EntityRenderData> map : getAllEntities().values()) {
            for (Int2ObjectMap.Entry<EntityRenderData> entry : map.int2ObjectEntrySet()) {
                if (isEntityInView(renderer, entry.getValue())) {
                    visibleEntities.add(entry.getValue());
                }
            }
        }
        visibleEntities.sort((a, b) -> Float.compare(b.renderY, a.renderY));

        for (EntityRenderData entity : visibleEntities) {
            ++visibleSize;
            EntityRenderers.getRenderer(entity).render(entity, renderer, batch, shapeRenderer, font, delta, debug);
        }

        if (batch != null) {
            batch.setShader(null);
        }
    }

    private boolean isEntityInView(Renderer renderer, EntityRenderData entity) {
        if (entity == null)
            return false;

        Camera camera = renderer.getCamera();
        float zoom = camera instanceof OrthographicCamera orthographicCamera ? orthographicCamera.zoom : 1f;

        float halfWidth = (renderer.getCamera().viewportWidth * .5f) * zoom;
        float halfHeight = (renderer.getCamera().viewportHeight * .5f) * zoom;

        float viewMinX = camera.position.x - halfWidth;
        float viewMaxX = camera.position.x + halfWidth;
        float viewMinY = camera.position.y - halfHeight;
        float viewMaxY = camera.position.y + halfHeight;

        float dx = camera.position.x - entity.x;
        float dy = camera.position.y - entity.y;
        if (dx * dx + dy * dy > (RENDER_DISTANCE * zoom) * (RENDER_DISTANCE * zoom))
            return false;

        Sprite sprite = entity.sprite;
        if (sprite == null)
            return false;

        float entityMinX = entity.x;
        float entityMaxX = entity.x + sprite.getWidth();
        float entityMinY = entity.y;
        float entityMaxY = entity.y + sprite.getHeight();

        return entityMaxX >= viewMinX && entityMinX <= viewMaxX && entityMaxY >= viewMinY && entityMinY <= viewMaxY;
    }

    public void update(float delta) {
        visibleEntities.forEach(entityRenderData -> entityRenderData.tick(delta));
    }
}
