package dev.creoii.chaos.client.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import dev.creoii.chaos.client.render.entity.StatusText;
import dev.creoii.chaos.client.util.Renderable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import javax.annotation.Nullable;
import java.util.Iterator;

public class StatusTextManager implements Renderable, Disposable {
    public static final BitmapFont FONT = new BitmapFont();
    public static final Pool<StatusText> STATUS_TEXTS = new Pool<>(16) {
        @Override
        protected StatusText newObject() {
            return new StatusText();
        }
    };
    public static final ObjectList<StatusText> ACTIVE_STATUS_TEXTS = new ObjectArrayList<>();

    public void addStatusText(String text, float x, float y, float lifetime) {
        addStatusText(text, x, y, lifetime, Color.WHITE);
    }

    public void addStatusText(String text, float x, float y, float lifetime, Color color) {
        StatusText statusText = STATUS_TEXTS.obtain();
        statusText.setText(text);
        statusText.setPos(x, y);
        statusText.setLifetime(lifetime);
        statusText.setColor(color);
        ACTIVE_STATUS_TEXTS.add(statusText);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        if (batch == null || ACTIVE_STATUS_TEXTS.isEmpty())
            return;

        Iterator<StatusText> iterator = ACTIVE_STATUS_TEXTS.iterator();
        while (iterator.hasNext()) {
            StatusText text = iterator.next();
            FONT.setColor(text.getColor());
            text.render(renderer, batch, shapeRenderer, FONT, delta, debug);
            if (text.getLifetime() <= 0) {
                iterator.remove();
                STATUS_TEXTS.free(text);
            }
        }
    }

    @Override
    public void dispose() {
        FONT.dispose();
    }
}
