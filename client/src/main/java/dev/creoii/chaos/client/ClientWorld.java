package dev.creoii.chaos.client;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import dev.creoii.chaos.World;
import dev.creoii.chaos.client.texture.TextureManager;

public class ClientWorld implements World {
    private final ClientGame game;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    public ClientWorld(ClientGame game, TiledMap map) {
        this.game = game;
        this.map = map;
        mapRenderer = new OrthogonalTiledMapRenderer(map, 4f);
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

    @Override
    public void dispose() {
        map.dispose();
    }
}
