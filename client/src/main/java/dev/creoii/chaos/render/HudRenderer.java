package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.creoii.chaos.CommandManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public class HudRenderer implements Renderable {
    public static final int TEXT_PADDING = 10;
    public static final GlyphLayout DEBUG_LAYOUT = new GlyphLayout();

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null) {
            CommandManager commandManager = renderer.getGame().getCommandManager();
            if (commandManager.isActive()) {
                font.draw(batch, "> " + commandManager.getCommand() + ((System.currentTimeMillis() / 400) % 2 == 0 ? "_" : ""), TEXT_PADDING, font.getCapHeight() + TEXT_PADDING);
            }

            if (debug) {
                CharacterEntityRenderData character = renderer.getGame().getCharacter();
                String posText = String.format("%.2f, %.2f", character.x / Entity.COORDINATE_SCALE, character.y / Entity.COORDINATE_SCALE);
                String statsText = "";//character.getStats().toDebugString(character.getMaxStats());

                String[] lines = new String[]{Gdx.graphics.getFramesPerSecond() + " FPS", posText, statsText};

                Viewport viewport = renderer.getViewport();

                float baseY = viewport.getWorldHeight() - TEXT_PADDING;
                float x;
                float y;

                for (int i = 0; i < lines.length; i++) {
                    String text = lines[i];
                    DEBUG_LAYOUT.setText(font, text);
                    x = viewport.getWorldWidth() - DEBUG_LAYOUT.width - TEXT_PADDING;
                    y = baseY - (i * 25);
                    font.draw(batch, DEBUG_LAYOUT, x, y);
                }
            }
        } else if (shapeRenderer != null) {
            /*LivingEntityData character = renderer.getGame().getCharacter();

            int health = character.getStats().health.value();
            int maxHealth = character.getMaxStats().health.value();

            Viewport viewport = renderer.getViewport();
            float screenWidth = viewport.getWorldWidth();

            float maxBarWidth = screenWidth * .5f;
            float barHeight = 15f;
            float healthPercentage = (float) health / maxHealth;
            float barWidth = maxBarWidth * healthPercentage;

            float x = (screenWidth / 2f) - (barWidth / 2f);
            float y = viewport.getWorldHeight() - barHeight - 10f;

            Color barColor = healthPercentage > .5f ? Color.GREEN : healthPercentage > .25f ? Color.ORANGE : Color.RED;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect((screenWidth / 2f) - (maxBarWidth / 2f), y, maxBarWidth, barHeight);
            shapeRenderer.setColor(barColor);
            shapeRenderer.rect(x, y, barWidth, barHeight);
            shapeRenderer.end();*/
        }
    }
}
