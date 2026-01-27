package dev.creoii.chaos;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

public interface World extends Disposable {
    String LAYER_GROUND = "ground";
    String LAYER_OBJECT = "object";

    Game getGame();

    Random getRandom();

    EntityManager<?> getEntityManager();

    TiledMap getMap();

    default MapLayer getLayer(int index) {
        return getMap().getLayers().get(index);
    }

    default MapLayer getLayer(String name) {
        return getMap().getLayers().get(name);
    }

    void setGround(int x, int y, String tile);

    default void setGroundArea(int x1, int y1, int x2, int y2, String tile) {
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                setGround(x, y, tile);
            }
        }
    }

    void setObject(int x, int y, String tile);

    default void setObjectArea(int x1, int y1, int x2, int y2, String tile) {
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                setObject(x, y, tile);
            }
        }
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
}
