package dev.creoii.chaos.client;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import dev.creoii.chaos.World;
import dev.creoii.chaos.client.chat.ClientChatManager;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.WorldRenderer;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.client.light.CreoRayHandler;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.world.setpiece.Setpiece;
import dev.creoii.chaos.world.tile.Tile;

import java.util.Random;

public class ClientWorld implements World {
    private final long seed;
    private final Random random;
    private final ClientGame game;
    private final WorldRenderer worldRenderer;
    private final ClientWorldListener listener;
    protected NetworkQueue<Object> networkQueue;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final EntityRenderManager entityManager;
    private final CreoRayHandler rayHandler;
    private final ClientChatManager chatManager;

    public ClientWorld(ClientGame game, TiledMap map, long seed) {
        this.game = game;
        this.map = map;
        this.seed = seed;
        random = new Random(seed);
        entityManager = new EntityRenderManager(this);
        worldRenderer = new WorldRenderer(this);
        listener = new ClientWorldListener(this);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 4f);
        rayHandler = new CreoRayHandler();
        chatManager = new ClientChatManager(this);

        game.getClient().addListener(listener);
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    @Override
    public ClientGame getGame() {
        return game;
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public ClientWorldListener getListener() {
        return listener;
    }

    public NetworkQueue<Object> getNetworkQueue() {
        return networkQueue;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public EntityRenderManager getEntityManager() {
        return entityManager;
    }

    public CreoRayHandler getRayHandler() {
        return rayHandler;
    }

    @Override
    public ClientChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public void setGround(int x, int y, Tile tile) {
        if (tile == null)
            return;
        MapLayer mapLayer = getLayer(LAYER_GROUND);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            StaticTiledMapTile tiledMapTile = new StaticTiledMapTile(new TextureRegion(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.ENVIRONMENT, tile.texture())));
            tiledMapTile.getProperties().put("id", tile.id());
            cell.setTile(tiledMapTile);
            tiledMapTileLayer.setCell(x, y, cell);

            if (tile.hasLight()) {
                PointLight light = new PointLight(getRayHandler(), tile.light().rays(), tile.light().color(), tile.light().distance(), x / Entity.COORDINATE_SCALE, y / Entity.COORDINATE_SCALE);
                light.setStaticLight(tile.light().isStatic());
                light.setSoft(tile.light().soft());
                light.setXray(true);
                light.setSoftnessLength(0f);
            }
        }
    }

    @Override
    public void setObject(int x, int y, Tile tile) {
        if (tile == null)
            return;
        MapLayer mapLayer = getLayer(LAYER_OBJECT);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            StaticTiledMapTile tiledMapTile = new StaticTiledMapTile(new TextureRegion(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.ENVIRONMENT, tile.texture())));
            tiledMapTile.getProperties().put("id", tile.id());
            cell.setTile(tiledMapTile);
            tiledMapTileLayer.setCell(x, y, cell);

            if (tile.hasLight()) {
                PointLight light = new PointLight(getRayHandler(), tile.light().rays(), tile.light().color(), tile.light().distance(), x, y);
                light.setStaticLight(tile.light().isStatic());
                light.setSoft(tile.light().soft());
                light.setXray(true);
                light.setSoftnessLength(0f);
            }
        }
    }

    @Override
    public void placeSetpiece(Setpiece setpiece, int x, int y) {
        setpiece.place(this, x, y);
        World.super.placeSetpiece(setpiece, x, y);
    }

    public void render(float delta, Renderer renderer, boolean debug) {
        entityManager.update(delta);

        if (networkQueue != null) {
            Object packet;
            while ((packet = networkQueue.queue().poll()) != null) {
                listener.handlePacket(networkQueue.connection(), packet);
            }
        }

        worldRenderer.render(delta, renderer, debug);
    }

    public void renderLight(float delta, Renderer renderer, boolean debug) {
        worldRenderer.renderLight(delta, renderer, debug);
    }

    @Override
    public void dispose() {
        map.dispose();
        rayHandler.dispose();
    }
}
