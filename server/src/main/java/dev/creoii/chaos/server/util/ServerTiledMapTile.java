package dev.creoii.chaos.server.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class ServerTiledMapTile implements TiledMapTile {
    private MapProperties properties;

    public ServerTiledMapTile() {

    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void setId(int i) {
    }

    @Override
    public BlendMode getBlendMode() {
        return null;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
    }

    @Override
    public TextureRegion getTextureRegion() {
        return null;
    }

    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
    }

    @Override
    public float getOffsetX() {
        return 0;
    }

    @Override
    public void setOffsetX(float v) {
    }

    @Override
    public float getOffsetY() {
        return 0;
    }

    @Override
    public void setOffsetY(float v) {
    }

    @Override
    public MapProperties getProperties() {
        if (properties == null) {
            properties = new MapProperties();
        }
        return properties;
    }

    @Override
    public MapObjects getObjects() {
        return null;
    }
}
