package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.creoii.chaos.client.chat.ChatManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.render.entity.data.EntityRenderData;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.util.Renderable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.event.ChangeStatEvent;
import dev.creoii.chaos.util.event.DamageEntityEvent;
import dev.creoii.chaos.util.event.SpawnEntityEvent;
import dev.creoii.chaos.util.stat.Stat;

import javax.annotation.Nullable;
import java.util.List;

public class HudRenderer implements Renderable {
    public static final int TEXT_PADDING = 10;
    public static final GlyphLayout CHAT_LAYOUT = new GlyphLayout();
    public static final GlyphLayout DEBUG_LAYOUT = new GlyphLayout();

    private ProgressBar healthBar;

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        if (batch != null) {
            renderChat(renderer, batch, font);

            if (debug) {
                String[] lines = getDebugText(renderer);

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
        }
    }

    @Override
    public void setupStage(Renderer renderer, Stage stage, Skin skin, boolean debug) {
        healthBar = new ProgressBar(0f, 100f, 1f, false, skin);

        SpawnEntityEvent.EVENT.register((_, entity) -> {
            if (entity == renderer.getGame().getCharacterId()) {
                EntityRenderData entityData = renderer.getGame().getEntityManager().getEntityData(EntityGroup.CHARACTER, entity);
                if (entityData instanceof CharacterEntityRenderData characterEntityRenderData) {
                    int health = characterEntityRenderData.statContainer.health().value();
                    int maxHealth = characterEntityRenderData.maxStatContainer.health().value();

                    Viewport viewport = renderer.getViewport();
                    float screenWidth = viewport.getWorldWidth();

                    float maxBarWidth = screenWidth * .5f;
                    float barHeight = 20f;
                    float healthPercentage = (float) health / maxHealth;
                    float barWidth = maxBarWidth * healthPercentage;

                    float x = (screenWidth / 2f) - (barWidth / 2f);
                    float y = viewport.getWorldHeight() - barHeight - 10f;

                    healthBar.setPosition(x, y);
                    healthBar.setSize(barWidth, barHeight);
                    healthBar.setValue(100f);
                }
            }
        });

        DamageEntityEvent.EVENT.register((_, _, entity, _) -> {
            if (entity == renderer.getGame().getCharacterId()) {
                EntityRenderData entityData = renderer.getGame().getEntityManager().getEntityData(EntityGroup.CHARACTER, entity);
                if (entityData instanceof CharacterEntityRenderData characterEntityRenderData) {
                    int health = characterEntityRenderData.statContainer.health().value();
                    int maxHealth = characterEntityRenderData.maxStatContainer.health().value();

                    float healthPercentage = (float) health / maxHealth;

                    if (healthPercentage < .25f) {
                        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.RED);
                        pixmap.fill();
                        healthBar.getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                        healthBar.getStyle().knobBefore.setMinHeight(20f);
                    } else if (healthPercentage < .5f) {
                        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.ORANGE);
                        pixmap.fill();
                        healthBar.getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                        healthBar.getStyle().knobBefore.setMinHeight(20f);
                    }

                    healthBar.setValue(healthPercentage * 100f);
                }
            }
        });

        ChangeStatEvent.EVENT.register((_, entity, stat) -> {
            if (stat.type() == Stat.Type.HEALTH && entity == renderer.getGame().getCharacterId()) {
                EntityRenderData entityData = renderer.getGame().getEntityManager().getEntityData(EntityGroup.CHARACTER, entity);
                if (entityData instanceof CharacterEntityRenderData characterEntityRenderData) {
                    int health = characterEntityRenderData.statContainer.health().value();
                    int maxHealth = characterEntityRenderData.maxStatContainer.health().value();

                    float healthPercentage = (float) health / maxHealth;

                    if (healthPercentage < .25f) {
                        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.RED);
                        pixmap.fill();
                        healthBar.getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                        healthBar.getStyle().knobBefore.setMinHeight(20f);
                    } else if (healthPercentage < .5f) {
                        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.ORANGE);
                        pixmap.fill();
                        healthBar.getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                        healthBar.getStyle().knobBefore.setMinHeight(20f);
                    }

                    healthBar.setValue(healthPercentage * 100f);
                }
            }
        });

        stage.addActor(healthBar);
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

    private static String[] getDebugText(Renderer renderer) {
        CharacterEntityRenderData character = renderer.getGame().getCharacter();
        String posText = String.format("%.2f, %.2f", character.x / Entity.COORDINATE_SCALE, character.y / Entity.COORDINATE_SCALE);
        String statsText = character.statContainer.toDebugString(character.maxStatContainer);
        EntityRenderManager entityRenderManager = renderer.getGame().getEntityManager();
        String entitiesText = "E:" + entityRenderManager.getVisibleSize() + "/" + entityRenderManager.getSize();
        String levelText = "L:" + character.level + (character.level >= 40 ? "" : "|LE:" + character.experience + "/" + Math.pow((character.level + 1) / .1f, 2));
        return new String[]{Gdx.graphics.getFramesPerSecond() + " FPS", posText, statsText, entitiesText, levelText};
    }
}
