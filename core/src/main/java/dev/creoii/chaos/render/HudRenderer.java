package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public class HudRenderer implements Renderable {
    public static final int TEXT_PADDING = 10;
    public static final GlyphLayout DEBUG_LAYOUT = new GlyphLayout();

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null) {
            CommandManager commandManager = renderer.getMain().getGame().getCommandManager();
            if (commandManager.isActive()) {
                font.draw(batch, "> " + commandManager.getCommand() + ((System.currentTimeMillis() / 400) % 2 == 0 ? "_" : ""), TEXT_PADDING, font.getCapHeight() + TEXT_PADDING);
            }

            if (debug) {
                CharacterEntity character = renderer.getMain().getGame().getActiveCharacter();
                String posText = String.format("%.2f, %.2f", character.getPos().x / Entity.COORDINATE_SCALE, character.getPos().y / Entity.COORDINATE_SCALE);
                String statsText = character.getStats().toDebugString(character.getMaxStats());

                String[] lines = new String[]{Gdx.graphics.getFramesPerSecond() + " FPS", posText, statsText};

                float baseY = Gdx.graphics.getHeight() - TEXT_PADDING;
                float x;
                float y;

                for (int i = 0; i < lines.length; i++) {
                    String text = lines[i];
                    DEBUG_LAYOUT.setText(font, text);
                    x = Gdx.graphics.getWidth() - DEBUG_LAYOUT.width - TEXT_PADDING;
                    y = baseY - (i * 25);
                    font.draw(batch, DEBUG_LAYOUT, x, y);
                }
            }
        }
    }
}
