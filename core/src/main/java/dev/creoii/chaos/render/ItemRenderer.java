package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.item.Item;

import javax.annotation.Nullable;

public class ItemRenderer {
    private static final float TOOLTIP_OFFSCREEN_PADDING = 4f;
    private static final BitmapFont FONT = new BitmapFont();

    public static void renderItem(SpriteBatch batch, Item item, Vector2 pos, float scale) {
        item.getSprite().setPosition(pos.x, pos.y);
        item.getSprite().setSize(scale, scale);
        item.getSprite().draw(batch);
    }

    public static void renderTooltip(@Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, Item item) {
        FONT.setColor(item.getRarity().getColor());

        String tooltip = item.id();
        GlyphLayout layout = new GlyphLayout(FONT, tooltip);

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        float tooltipWidth = layout.width + 2 * TOOLTIP_OFFSCREEN_PADDING;
        float tooltipHeight = layout.height + 2 * TOOLTIP_OFFSCREEN_PADDING;

        float x = mousePos.x + TOOLTIP_OFFSCREEN_PADDING;
        float y = mousePos.y + tooltipHeight;

        if (x + tooltipWidth > Gdx.graphics.getWidth())
            x = Gdx.graphics.getWidth() - tooltipWidth;
        if (y > Gdx.graphics.getHeight())
            y = Gdx.graphics.getHeight();

        if (batch != null) {
            FONT.draw(batch, layout, x + TOOLTIP_OFFSCREEN_PADDING, y - TOOLTIP_OFFSCREEN_PADDING);
        }
    }

    static {
        FONT.setUseIntegerPositions(false);
        FONT.getData().setScale(1.5f);
    }
}
