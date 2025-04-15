package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.item.Item;

public class ItemRenderer {
    public static void renderItem(Main main, SpriteBatch batch, BitmapFont font, Item item, Vector2 pos, float scale, boolean showTooltip) {
        item.getSprite().setPosition(pos.x, pos.y);
        item.getSprite().setSize(scale, scale);
        item.getSprite().draw(batch);

        if (showTooltip) {
            String tooltip = item.id();
            GlyphLayout layout = new GlyphLayout(font, tooltip);

            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            float padding = 4f;

            font.draw(batch, tooltip, mousePos.x + padding, mousePos.y + layout.height + padding);
        }
    }
}
