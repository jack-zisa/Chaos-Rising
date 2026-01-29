package dev.creoii.chaos;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.util.event.PlaceSetpieceEvent;
import dev.creoii.chaos.world.setpiece.Setpiece;
import dev.creoii.chaos.world.tile.Tile;

import javax.annotation.Nullable;
import java.util.Random;

public interface World extends Disposable {
    String LAYER_GROUND = "ground";
    String LAYER_OBJECT = "object";

    Game getGame();

    long getSeed();

    Random getRandom();

    EntityManager<?> getEntityManager();

    TiledMap getMap();

    @Nullable
    default MapLayer getLayer(int index) {
        return getMap().getLayers().get(index);
    }

    @Nullable
    default MapLayer getLayer(String name) {
        return getMap().getLayers().get(name);
    }

    void setGround(int x, int y, Tile tile);

    default void setGroundArea(int x1, int y1, int x2, int y2, Tile tile) {
        if (tile == null)
            return;
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                setGround(x, y, tile);
            }
        }
    }

    void setObject(int x, int y, Tile tile);

    default void setObjectArea(int x1, int y1, int x2, int y2, Tile tile) {
        if (tile == null)
            return;
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                setObject(x, y, tile);
            }
        }
    }

    @Nullable
    default Tile getGround(int x, int y) {
        MapLayer mapLayer = getLayer(LAYER_GROUND);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);
            if (cell != null) {
                Object idObj = cell.getTile().getProperties().get("id");

                if (idObj instanceof String s) {
                    return DataManager.getTile(s);
                }
            }
        }
        return null;
    }

    static TiledMap createMapOfSize(int x, int y) {
        TiledMap map = new TiledMap();
        TiledMapTileLayer ground = new TiledMapTileLayer(x, y, 8, 8);
        TiledMapTileLayer object = new TiledMapTileLayer(x, y, 8, 8);
        ground.setName(LAYER_GROUND);
        object.setName(LAYER_OBJECT);
        map.getLayers().add(ground);
        map.getLayers().add(object);
        return map;
    }

    default void placeSetpiece(Setpiece setpiece, int x, int y) {
        PlaceSetpieceEvent.EVENT.invoker().onSpawnEntity(this, setpiece, x, y);
    }
}
