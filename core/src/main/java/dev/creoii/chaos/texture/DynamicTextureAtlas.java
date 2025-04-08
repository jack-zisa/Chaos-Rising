package dev.creoii.chaos.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class DynamicTextureAtlas {
    private TextureAtlas textureAtlas;
    private ObjectMap<String, TextureRegion> textures;

    public DynamicTextureAtlas() {
        textureAtlas = new TextureAtlas();
        textures = new ObjectMap<>();
    }

    /**
     * Load and add a texture to the dynamic atlas at runtime.
     *
     * @param texturePath - Path to the texture file (e.g., "assets/textures/characters/walk.png")
     * @param textureId - The id for the texture (e.g., "walk")
     */
    public void addTexture(String texturePath, String textureId) {
        Texture texture = new Texture(texturePath);  // Load the texture from file
        TextureRegion region = new TextureRegion(texture);
        textureAtlas.addRegion(textureId, region);   // Add it to the atlas under a specific ID
        textures.put(textureId, region);             // Store the texture in the map for future use
    }

    /**
     * Retrieve a texture by its ID from the dynamic atlas.
     */
    public TextureRegion getTexture(String textureId) {
        return textures.get(textureId);
    }

    /**
     * Get the texture atlas for drawing.
     */
    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void dispose() {
        textureAtlas.dispose();  // Dispose the atlas when done
    }
}
