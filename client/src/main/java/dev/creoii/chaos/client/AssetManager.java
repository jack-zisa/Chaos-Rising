package dev.creoii.chaos.client;

import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.util.logging.Logger;

public class AssetManager implements Disposable {
    public static final Logger LOGGER = new Logger(AssetManager.class.getSimpleName());
    private final com.badlogic.gdx.assets.AssetManager manager;
    private final TextureManager textureManager;

    public AssetManager() {
        manager = new com.badlogic.gdx.assets.AssetManager();
        textureManager = new TextureManager();
    }

    public com.badlogic.gdx.assets.AssetManager getManager() {
        return manager;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public void load() {
        textureManager.load(manager);
        manager.finishLoading();
        textureManager.finish(manager);
    }

    @Override
    public void dispose() {
        manager.dispose();
        textureManager.dispose();
    }
}
