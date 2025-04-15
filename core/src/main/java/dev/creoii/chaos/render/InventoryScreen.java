package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.render.screen.Screen;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    private static final float SLOT_SIZE = 49f;
    private final Vector2 pos;
    private final Inventory inventory;
    private final Sprite slotSprite;

    public InventoryScreen(Vector2 pos, Inventory inventory) {
        super("Inventory");
        this.pos = pos;
        this.inventory = inventory;
        slotSprite = new Sprite(new Texture("textures/ui/slot.png"));
        slotSprite.setSize(SLOT_SIZE, SLOT_SIZE);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch == null)
            return;

        for (int r = 0; r < inventory.getInventory().length; ++r) {
            for (int c = 0; c < inventory.getInventory()[r].length; ++c) {
                slotSprite.setPosition(pos.x + (c * SLOT_SIZE), pos.y + (r * SLOT_SIZE));
                slotSprite.draw(batch);
                Slot slot = inventory.getInventory()[r][c];
                if (slot != null && slot.getStack() != null && slot.getStack().getItem() != null) {
                    Vector2 pos = new Vector2(this.pos.x + 3, this.pos.y + 3);
                    ItemRenderer.renderItem(renderer.getMain(), batch, font, slot.getStack().getItem(), pos, 47f, isMouseOverSlot(pos));
                }
            }
        }
    }

    public static boolean isMouseOverSlot(Vector2 pos) {
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return Gdx.input.getX() >= pos.x && Gdx.input.getX() <= pos.x + SLOT_SIZE && mouseY >= pos.y && mouseY <= pos.y + SLOT_SIZE;
    }
}
