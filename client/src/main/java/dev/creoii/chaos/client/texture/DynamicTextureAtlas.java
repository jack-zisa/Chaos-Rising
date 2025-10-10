package dev.creoii.chaos.client.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class DynamicTextureAtlas {
    private final TextureAtlas textureAtlas;
    private final ObjectMap<String, TextureRegion> textures;
    private final ObjectMap<String, String> pendingTextures;

    public DynamicTextureAtlas() {
        textureAtlas = new TextureAtlas();
        textures = new ObjectMap<>();
        pendingTextures = new ObjectMap<>();
    }

    protected ObjectMap<String, String> getPendingTextures() {
        return pendingTextures;
    }

    public void addTexture(String path, String id) {
        pendingTextures.put(path, id);
        /*TextureRegion region = new TextureRegion(new Texture(path));
        textureAtlas.addRegion(id, region);
        textures.put(id, region);*/
    }

    public void bindTextures(AssetManager assetManager) {
        for (ObjectMap.Entry<String, String> entry : pendingTextures.entries()) {
            String path = entry.key;
            String id = entry.value;

            if (assetManager.isLoaded(path, Texture.class)) {
                Texture texture = assetManager.get(path, Texture.class);
                TextureRegion region = new TextureRegion(texture);

                textureAtlas.addRegion(id, region);
                textures.put(id, region);
            }
        }

        pendingTextures.clear();
    }

    public TextureRegion getTexture(String id) {
        return textures.get(id, TextureManager.DEFAULT_TEXTURE_REGION);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
