package dev.creoii.chaos.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.DataManager;

public class TextureManager implements Disposable {
    private static final String[] ATLAS_IDS = new String[]{"class", "enemy", "bullet", "item", "loot"};
    public static final String DEFAULT_TEXTURE_ID = "textures/missing.png";
    protected static final Texture DEFAULT_TEXTURE = new Texture(DEFAULT_TEXTURE_ID);
    protected static final TextureRegion DEFAULT_TEXTURE_REGION = new TextureRegion(DEFAULT_TEXTURE);
    private final ObjectMap<String, DynamicTextureAtlas> atlases;

    public TextureManager() {
        atlases = new ObjectMap<>();
        for (String atlasId : ATLAS_IDS) {
            atlases.put(atlasId, new DynamicTextureAtlas());
        }
    }

    public Texture getTexture(String atlas, String texture) {
        if (!atlases.containsKey(atlas))
            return DEFAULT_TEXTURE;
        return atlases.get(atlas).getTexture(texture).getTexture();
    }

    public void load() {
        FileHandle baseDir = Gdx.files.internal("textures");

        if (!baseDir.exists()) {
            Gdx.app.log(Texture.class.getSimpleName(), "Directory 'textures/' does not exist.");
            return;
        }

        for (String atlasId : ATLAS_IDS) {
            FileHandle folderHandle = baseDir.child(atlasId);
            if (!folderHandle.exists()) {
                Gdx.app.log(DataManager.class.getSimpleName(), "Folder '" + folderHandle.path() + "' does not exist, skipping.");
                continue;
            }

            for (FileHandle file : folderHandle.list("png")) {
                try {
                    String path = file.path();
                    atlases.get(atlasId).addTexture(path, path.substring(10 + atlasId.length(), path.length() - 4));
                } catch (Exception e) {
                    Gdx.app.error(DataManager.class.getSimpleName(), "Error parsing " + file.name() + " in " + atlasId + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void dispose() {
        for (DynamicTextureAtlas atlas : atlases.values()) {
            atlas.getTextureAtlas().dispose();
        }
    }
}
