package dev.creoii.chaos.client.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class TextureManager implements Disposable {
    private static final String[] ATLAS_IDS = new String[]{"character", "enemy", "bullet", "item", "loot_drop", "effect", "ui"};
    public static final String DEFAULT_TEXTURE_ID = "textures/misc/missing.png";
    protected static final Texture DEFAULT_TEXTURE = new Texture(DEFAULT_TEXTURE_ID);
    protected static final TextureRegion DEFAULT_TEXTURE_REGION = new TextureRegion(DEFAULT_TEXTURE);
    private final ObjectMap<String, DynamicTextureAtlas> atlases;

    public TextureManager() {
        atlases = new ObjectMap<>();
    }

    public ObjectMap<String, DynamicTextureAtlas> getAtlases() {
        return atlases;
    }

    public Texture getTexture(String atlas, String texture) {
        if (!atlases.containsKey(atlas))
            return DEFAULT_TEXTURE;
        return atlases.get(atlas).getTexture(texture).getTexture();
    }

    public void load(AssetManager assetManager) {
        FileHandle baseDir = Gdx.files.internal("textures");

        if (!baseDir.exists()) {
            Gdx.app.log(Texture.class.getSimpleName(), "Directory 'textures/' does not exist.");
            return;
        }

        for (String atlasId : ATLAS_IDS) {
            DynamicTextureAtlas atlas = new DynamicTextureAtlas();
            atlases.put(atlasId, atlas);

            FileHandle folderHandle = baseDir.child(atlasId);
            if (!folderHandle.exists()) {
                Gdx.app.log(TextureManager.class.getSimpleName(), "Folder '" + folderHandle.path() + "' does not exist, skipping.");
                continue;
            }

            for (FileHandle file : folderHandle.list("png")) {
                try {
                    String path = file.path();
                    String id = path.substring(10 + atlasId.length(), path.length() - 4);

                    assetManager.load(path, Texture.class);
                    atlas.addTexture(path, id);
                } catch (Exception e) {
                    Gdx.app.error(TextureManager.class.getSimpleName(), "Error parsing " + file.name() + " in " + atlasId + ": " + e.getMessage());
                }
            }

            dev.creoii.chaos.client.AssetManager.LOGGER.info("Created texture atlas '" + atlasId + "' size: " + atlas.getPendingTextures().size);
        }
    }

    public void finish(AssetManager assetManager) {
        for (String atlasId : ATLAS_IDS) {
            atlases.get(atlasId).bindTextures(assetManager);
        }
    }

    @Override
    public void dispose() {
        for (DynamicTextureAtlas atlas : atlases.values()) {
            atlas.getTextureAtlas().dispose();
        }
    }
}
