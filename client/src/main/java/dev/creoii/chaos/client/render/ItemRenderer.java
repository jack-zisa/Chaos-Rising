package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.tooltip.Tooltip;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRenderer implements Disposable {
    private static final float TOOLTIP_OFFSCREEN_PADDING = 4f;
    private static final BitmapFont SECTION_FONT = new BitmapFont();
    private static final Map<Tooltip.Section, BitmapFont> FONTS = new HashMap<>();

    public static void renderItem(ClientGame game, SpriteBatch batch, @Nullable String id, Vector2 pos, float scale) {
        if (id == null || id.isBlank())
            return;
        Sprite sprite = new Sprite(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.ITEM, id));
        sprite.setPosition(pos.x, pos.y);
        sprite.setSize(scale, scale);
        batch.enableBlending();
        sprite.draw(batch);
        batch.disableBlending();
    }

    public static void renderTooltip(@Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, @Nullable Item item) {
        if (item == null)
            return;

        Tooltip tooltip = item.getTooltip();

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        float x = mousePos.x + TOOLTIP_OFFSCREEN_PADDING;
        float y = mousePos.y + TOOLTIP_OFFSCREEN_PADDING;

        float maxWidth = 0f;
        float totalHeight = 0f;

        for (Map.Entry<Tooltip.Section, List<String>> entry : tooltip.sections().entrySet()) {
            Tooltip.Section section = entry.getKey();
            List<String> lines = entry.getValue();

            GlyphLayout layout = new GlyphLayout(SECTION_FONT, section.name());
            maxWidth = Math.max(maxWidth, layout.width);
            totalHeight += layout.height + 6f;

            for (String line : lines) {
                layout.setText(FONTS.get(section), line);
                maxWidth = Math.max(maxWidth, layout.width);
                totalHeight += layout.height + 2f;
            }

            totalHeight += 8f;
        }

        float tooltipWidth = maxWidth + 2 * TOOLTIP_OFFSCREEN_PADDING;
        float tooltipHeight = totalHeight + 2 * TOOLTIP_OFFSCREEN_PADDING;

        if (x + tooltipWidth > Gdx.graphics.getWidth())
            x = Gdx.graphics.getWidth() - tooltipWidth;
        if (y + tooltipHeight > Gdx.graphics.getHeight())
            y = Gdx.graphics.getHeight() - tooltipHeight;

        if (batch != null) {
            float drawY = y + tooltipHeight - TOOLTIP_OFFSCREEN_PADDING;

            for (Map.Entry<Tooltip.Section, List<String>> entry : tooltip.sections().entrySet()) {
                Tooltip.Section section = entry.getKey();
                BitmapFont font = FONTS.get(section);
                List<String> lines = entry.getValue();

                GlyphLayout layout = new GlyphLayout(SECTION_FONT, section.name());
                SECTION_FONT.draw(batch, layout, x + TOOLTIP_OFFSCREEN_PADDING, drawY);
                drawY -= layout.height + 6f;

                for (String line : lines) {
                    font.setColor(switch (section) {
                        case NAME, RARITY -> item.getRarity().getColor();
                        default -> Color.LIGHT_GRAY;
                    });
                    layout.setText(font, line);
                    font.draw(batch, layout, x + TOOLTIP_OFFSCREEN_PADDING, drawY);
                    drawY -= layout.height + 2f;
                }

                drawY -= 8f;
            }
        }
    }

    @Override
    public void dispose() {
        SECTION_FONT.dispose();

        for (BitmapFont font : FONTS.values()) {
            font.dispose();
        }
    }

    static {
        SECTION_FONT.getData().setScale(.8f);
        SECTION_FONT.setColor(Color.DARK_GRAY);

        for (Tooltip.Section section : Tooltip.Section.values()) {
            FONTS.put(section, new BitmapFont());
        }
    }
}
