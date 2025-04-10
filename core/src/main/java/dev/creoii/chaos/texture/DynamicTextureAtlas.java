package dev.creoii.chaos.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class DynamicTextureAtlas {
    private final TextureAtlas textureAtlas;
    private final ObjectMap<String, TextureRegion> textures;

    public DynamicTextureAtlas() {
        textureAtlas = new TextureAtlas();
        textures = new ObjectMap<>();
    }

    public void addTexture(String path, String id) {
        TextureRegion region = new TextureRegion(new Texture(path));
        textureAtlas.addRegion(id, region);
        textures.put(id, region);
    }

    public TextureRegion getTexture(String id) {
        if (!textures.containsKey(id))
            return TextureManager.DEFAULT_TEXTURE_REGION;
        return textures.get(id);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
