package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.item.Item;

import javax.annotation.Nullable;

public class ItemRenderer {
    private static final float TOOLTIP_OFFSCREEN_PADDING = 4f;
    private static final BitmapFont TITLE_FONT = new BitmapFont();
    private static final BitmapFont DESCRIPTION_FONT = new BitmapFont();
    private static final GlyphLayout TITLE_LAYOUT = new GlyphLayout();
    private static final GlyphLayout DESCRIPTION_LAYOUT = new GlyphLayout();

    public static void renderItem(ClientGame game, SpriteBatch batch, @Nullable String id, Vector2 pos, float scale) {
        if (id == null || id.isBlank())
            return;
        Sprite sprite = new Sprite(game.getAssetManager().getTextureManager().getTexture("item", id));
        sprite.setPosition(pos.x, pos.y);
        sprite.setSize(scale, scale);
        batch.enableBlending();
        sprite.draw(batch);
        batch.disableBlending();
    }

    public static void renderTooltip(@Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, @Nullable Item item) {
        if (item == null)
            return;

        String tooltip = item.getTooltip();
        int splitIndex = tooltip.indexOf('\n');
        TITLE_LAYOUT.setText(TITLE_FONT, tooltip.substring(0, splitIndex));
        DESCRIPTION_LAYOUT.setText(DESCRIPTION_FONT, tooltip.substring(splitIndex));

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        float tooltipWidth = Math.max(TITLE_LAYOUT.width, DESCRIPTION_LAYOUT.width) + 2 * TOOLTIP_OFFSCREEN_PADDING;
        float tooltipHeight = TITLE_LAYOUT.height + DESCRIPTION_LAYOUT.height + 3 * TOOLTIP_OFFSCREEN_PADDING;

        float x = mousePos.x + TOOLTIP_OFFSCREEN_PADDING;
        float y = mousePos.y + tooltipHeight;

        if (x + tooltipWidth > Gdx.graphics.getWidth())
            x = Gdx.graphics.getWidth() - tooltipWidth;
        if (y > Gdx.graphics.getHeight())
            y = Gdx.graphics.getHeight();

        if (batch != null) {
            TITLE_FONT.setColor(item.getRarity().getColor());
            TITLE_FONT.draw(batch, TITLE_LAYOUT, x + TOOLTIP_OFFSCREEN_PADDING, y - TOOLTIP_OFFSCREEN_PADDING);
            DESCRIPTION_FONT.draw(batch, DESCRIPTION_LAYOUT, x + TOOLTIP_OFFSCREEN_PADDING, y - TOOLTIP_OFFSCREEN_PADDING);
        }
    }

    static {
        TITLE_FONT.setUseIntegerPositions(false);
        TITLE_FONT.getData().setScale(1.5f);
        DESCRIPTION_FONT.setUseIntegerPositions(false);
        DESCRIPTION_FONT.setColor(Color.LIGHT_GRAY);
    }
}
