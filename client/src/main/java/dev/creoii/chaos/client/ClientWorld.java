package dev.creoii.chaos.client;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import dev.creoii.chaos.World;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.WorldRenderer;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.network.NetworkQueue;

import java.util.Random;

public class ClientWorld implements World {
    private static final Random RANDOM = new Random();
    private final ClientGame game;
    private final WorldRenderer worldRenderer;
    private final ClientWorldListener listener;
    protected NetworkQueue<Object> networkQueue;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final EntityRenderManager entityManager;

    public ClientWorld(ClientGame game, TiledMap map) {
        this.game = game;
        this.map = map;
        entityManager = new EntityRenderManager(this);
        worldRenderer = new WorldRenderer(this);
        listener = new ClientWorldListener(this);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 4f);

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
        return RANDOM;
    }

    @Override
    public EntityRenderManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setGround(int x, int y, String tile) {
        MapLayer mapLayer = getLayer(LAYER_GROUND);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(new StaticTiledMapTile(new TextureRegion(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.ENVIRONMENT, tile))));
            tiledMapTileLayer.setCell(x, y, cell);
        }
    }

    @Override
    public void setObject(int x, int y, String tile) {
        MapLayer mapLayer = getLayer(LAYER_OBJECT);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(new StaticTiledMapTile(new TextureRegion(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.ENVIRONMENT, tile))));
            tiledMapTileLayer.setCell(x, y, cell);
        }
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

    @Override
    public void dispose() {
        map.dispose();
    }
}
