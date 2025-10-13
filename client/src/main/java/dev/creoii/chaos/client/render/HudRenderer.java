package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.creoii.chaos.client.chat.ChatManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.util.Renderable;

import javax.annotation.Nullable;
import java.util.List;

public class HudRenderer implements Renderable {
    public static final int TEXT_PADDING = 10;
    public static final GlyphLayout CHAT_LAYOUT = new GlyphLayout();
    public static final GlyphLayout DEBUG_LAYOUT = new GlyphLayout();

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        if (batch != null) {
            renderChat(renderer, batch, font);

            if (debug) {
                String[] lines = getDebugText(renderer, batch);

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
            CharacterEntityRenderData character = renderer.getGame().getCharacter();

            int health = character.statContainer.health().value();
            int maxHealth = character.maxStatContainer.health().value();

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
            shapeRenderer.end();
        }
    }

    private static void renderChat(Renderer renderer, SpriteBatch batch, BitmapFont font) {
        font.getData().setScale(1.5f);

        ChatManager chatManager = renderer.getGame().getChatManager();
        if (chatManager.isActive()) {
            font.setColor(Color.WHITE);
            font.draw(batch, "> " + chatManager.getInput() + ((System.currentTimeMillis() / 400) % 2 == 0 ? "_" : ""), TEXT_PADDING, font.getCapHeight() + TEXT_PADDING);
            font.setColor(Color.LIGHT_GRAY);
        }

        List<Message> messages = chatManager.getMessages();
        int count = Math.min(20, messages.size());

        for (int i = count - 1; i >= 0; --i) {
            Message message = messages.get(messages.size() - 1 - i);
            String sender = "[" + (message.getSenderId() == -1 ? "Server" : message.getSenderId()) + "]: ";

            float x = TEXT_PADDING;
            float y = 25 + TEXT_PADDING + (i + 1) * 29;

            font.setColor(Color.YELLOW);
            CHAT_LAYOUT.setText(font, sender);
            font.draw(batch, CHAT_LAYOUT, x, y);

            float offsetX = CHAT_LAYOUT.width;

            font.setColor(message.getColor());
            CHAT_LAYOUT.setText(font, message.getText());
            font.draw(batch, CHAT_LAYOUT, x + offsetX, y);
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(2f);
    }

    private static String[] getDebugText(Renderer renderer, SpriteBatch batch) {
        CharacterEntityRenderData character = renderer.getGame().getCharacter();
        String posText = String.format("%.2f, %.2f", character.x / Entity.COORDINATE_SCALE, character.y / Entity.COORDINATE_SCALE);
        String statsText = character.statContainer.toDebugString(character.maxStatContainer);
        EntityRenderManager entityRenderManager = renderer.getGame().getEntityManager();
        String entitiesText = "E:" + entityRenderManager.getVisibleSize() + "/" + entityRenderManager.getSize();
        return new String[]{Gdx.graphics.getFramesPerSecond() + " FPS", posText, statsText, entitiesText};
    }
}
