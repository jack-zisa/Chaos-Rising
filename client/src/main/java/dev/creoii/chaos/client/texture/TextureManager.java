package dev.creoii.chaos.client.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.util.EntityGroup;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class TextureManager implements Disposable {
    public static final String DEFAULT_TEXTURE_ID = "textures/misc/missing.png";
    protected static final Texture DEFAULT_TEXTURE = new Texture(DEFAULT_TEXTURE_ID);
    protected static final TextureRegion DEFAULT_TEXTURE_REGION = new TextureRegion(DEFAULT_TEXTURE);
    private final Int2ObjectOpenHashMap<DynamicTextureAtlas> atlases;

    public TextureManager() {
        atlases = new Int2ObjectOpenHashMap<>();
    }

    public Int2ObjectOpenHashMap<DynamicTextureAtlas> getAtlases() {
        return atlases;
    }

    public Texture getTexture(AtlasKey atlas, String texture) {
        if (!atlases.containsKey(atlas.ordinal()))
            return DEFAULT_TEXTURE;
        return atlases.get(atlas.ordinal()).getTexture(texture).getTexture();
    }

    public void load(AssetManager assetManager) {
        FileHandle baseDir = Gdx.files.internal("textures");

        if (!baseDir.exists()) {
            Gdx.app.log(Texture.class.getSimpleName(), "Directory 'textures/' does not exist.");
            return;
        }

        for (AtlasKey key : AtlasKey.values()) {
            DynamicTextureAtlas atlas = new DynamicTextureAtlas();
            String name = key.name().toLowerCase();
            atlases.put(key.ordinal(), atlas);

            FileHandle folderHandle = baseDir.child(name);
            if (!folderHandle.exists()) {
                Gdx.app.log(TextureManager.class.getSimpleName(), "Folder '" + folderHandle.path() + "' does not exist, skipping.");
                continue;
            }

            for (FileHandle file : folderHandle.list("png")) {
                try {
                    String path = file.path();
                    String id = path.substring(10 + name.length(), path.length() - 4);

                    assetManager.load(path, Texture.class);
                    atlas.addTexture(path, id);
                } catch (Exception e) {
                    Gdx.app.error(TextureManager.class.getSimpleName(), "Error parsing " + file.name() + " in " + name + ": " + e.getMessage());
                }
            }

            dev.creoii.chaos.client.AssetManager.LOGGER.info("Created texture atlas '" + name + "' size: " + atlas.getPendingTextures().size);
        }
    }

    public void finish(AssetManager assetManager) {
        for (AtlasKey key : AtlasKey.values()) {
            atlases.get(key.ordinal()).bindTextures(assetManager);
        }
    }

    @Override
    public void dispose() {
        for (DynamicTextureAtlas atlas : atlases.values()) {
            atlas.getTextureAtlas().dispose();
        }
    }

    public enum AtlasKey {
        CHARACTER,
        ENEMY,
        BULLET,
        ITEM,
        LOOT_DROP,
        EFFECT,
        UI;

        public static AtlasKey fromEntityGroup(EntityGroup group) {
            return switch (group) {
                case CHARACTER -> AtlasKey.CHARACTER;
                case ENEMY -> AtlasKey.ENEMY;
                case BULLET -> AtlasKey.BULLET;
                case LOOT_DROP -> AtlasKey.LOOT_DROP;
            };
        }
    }
}
