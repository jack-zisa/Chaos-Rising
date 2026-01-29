package dev.creoii.chaos.client.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.logging.Logger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class TextureManager implements Disposable {
    public static final Logger LOGGER = new Logger(TextureManager.class.getSimpleName());
    public static final String DEFAULT_TEXTURE_ID = "textures/misc/missing.png";
    protected static final Texture DEFAULT_TEXTURE = new Texture(DEFAULT_TEXTURE_ID);
    protected static final TextureRegion DEFAULT_TEXTURE_REGION = new TextureRegion(DEFAULT_TEXTURE);
    private final Int2ObjectOpenHashMap<DynamicTextureAtlas> atlases;

    public TextureManager() {
        atlases = new Int2ObjectOpenHashMap<>();
    }

    public Texture getTexture(Atlas atlas, String texture) {
        if (!atlases.containsKey(atlas.ordinal())) {
            LOGGER.warn("Unknown texture requested from atlas '" + atlas.name() + ": " + texture);
            return DEFAULT_TEXTURE;
        }
        return atlases.get(atlas.ordinal()).getTexture(texture).getTexture();
    }

    public void load(AssetManager assetManager) {
        FileHandle baseDir = Gdx.files.internal("textures");

        if (!baseDir.exists()) {
            LOGGER.info("Directory 'textures/' does not exist.");
            return;
        }

        assetManager.setLoader(
            Texture.class,
            new ConditionalPaddedTextureLoader(new InternalFileHandleResolver())
        );

        FileHandle assets = Gdx.files.internal("assets.txt");

        for (String line : assets.readString().split("\\R")) {
            if (!line.startsWith("textures/") || !line.endsWith(".png"))
                continue;

            String path = line.trim();
            String[] parts = path.split("/");
            if (parts.length < 3)
                continue;

            try {
                Atlas atlas = Atlas.valueOf(parts[1].toUpperCase());

                TextureLoader.TextureParameter params = null;

                if (atlas.hasOutline()) {
                    params = new ConditionalPaddedTextureLoader.PaddedTextureParameter(2);
                }

                assetManager.load(path, Texture.class, params);

                if (!atlases.containsKey(atlas.ordinal())) {
                    atlases.put(atlas.ordinal(), new DynamicTextureAtlas());
                }

                atlases.get(atlas.ordinal()).addTexture(path, parts[2].replace(".png", ""));
            } catch (IllegalArgumentException _) {

            }
        }

        for (Atlas key : Atlas.values()) {
            LOGGER.info("Created dynamic atlas '" + key.name().toLowerCase() + "' size: " + atlases.get(key.ordinal()).getPendingTextures().size);
        }
    }

    public void finish(AssetManager assetManager) {
        for (Atlas atlas : Atlas.values()) {
            atlases.get(atlas.ordinal()).bindTextures(assetManager);
        }
    }

    @Override
    public void dispose() {
        for (DynamicTextureAtlas atlas : atlases.values()) {
            atlas.getTextureAtlas().dispose();
        }
    }

    public enum Atlas {
        CHARACTER(true),
        ENEMY(true),
        BULLET(true),
        ITEM(true),
        LOOT_DROP(true),
        EFFECT,
        UI,
        ENVIRONMENT;

        private final boolean hasOutline;

        Atlas(boolean hasOutline) {
            this.hasOutline = hasOutline;
        }

        Atlas() {
            this(false);
        }

        public boolean hasOutline() {
            return hasOutline;
        }

        public static Atlas fromEntityGroup(EntityGroup group) {
            return switch (group) {
                case CHARACTER -> Atlas.CHARACTER;
                case ENEMY, OBJECT -> Atlas.ENEMY;
                case BULLET -> Atlas.BULLET;
                case LOOT_DROP -> Atlas.LOOT_DROP;
            };
        }
    }
}
