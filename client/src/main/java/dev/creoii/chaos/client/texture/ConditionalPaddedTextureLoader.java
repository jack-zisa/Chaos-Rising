package dev.creoii.chaos.client.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ConditionalPaddedTextureLoader extends TextureLoader {
    public ConditionalPaddedTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
        int padding = 0;

        if (parameter instanceof PaddedTextureParameter p) {
            padding = p.getPadding();
        }

        if (padding <= 0) {
            return new Texture(file);
        }

        Pixmap original = new Pixmap(file);
        Pixmap padded = new Pixmap(original.getWidth() + padding * 2, original.getHeight() + padding * 2, original.getFormat());

        padded.setColor(0, 0, 0, 0);
        padded.fill();
        padded.drawPixmap(original, padding, padding);

        Texture texture = new Texture(padded);

        original.dispose();
        padded.dispose();

        return texture;
    }

    public static class PaddedTextureParameter extends TextureLoader.TextureParameter {
        public final int padding;

        public PaddedTextureParameter(int padding) {
            this.padding = padding;
        }

        public int getPadding() {
            return padding;
        }
    }
}
