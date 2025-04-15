package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.render.ItemRenderer;
import dev.creoii.chaos.render.Renderer;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    private static final float SLOT_SIZE = 49f;
    private final Inventory inventory;
    private final Sprite slotSprite;

    public InventoryScreen(Vector2 pos, Inventory inventory) {
        super("Inventory", pos, (inventory.getInventory().length * 48f) + 31f);
        this.inventory = inventory;
        slotSprite = new Sprite(new Texture("textures/ui/slot.png"));
        slotSprite.setSize(SLOT_SIZE, SLOT_SIZE);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        super.render(renderer, batch, shapeRenderer, font, debug);

        if (batch == null)
            return;

        for (int r = 0; r < inventory.getInventory().length; ++r) {
            for (int c = 0; c < inventory.getInventory()[r].length; ++c) {
                slotSprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                slotSprite.draw(batch);
                Slot slot = inventory.getInventory()[r][c];
                if (slot != null && slot.getStack() != null && slot.getStack().getItem() != null) {
                    ItemRenderer.renderItem(batch, slot.getStack().getItem(), new Vector2(getPos().x + 3, getPos().y + 3), 42f);
                }
            }
        }

        Slot mouseOverSlot = getMouseOverSlot();
        if (mouseOverSlot != null && mouseOverSlot.getStack() != null && mouseOverSlot.getStack().getItem() != null) {
            ItemRenderer.renderTooltip(batch, font, mouseOverSlot.getStack().getItem());
        }
    }

    public Slot getMouseOverSlot() {
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (int r = 0; r < inventory.getInventory().length; ++r) {
            for (int c = 0; c < inventory.getInventory()[r].length; ++c) {
                float slotX = getPos().x + (c * SLOT_SIZE);
                float slotY = getPos().y + (r * SLOT_SIZE);

                if (Gdx.input.getX() >= slotX && Gdx.input.getX() <= slotX + SLOT_SIZE && mouseY >= slotY && mouseY <= slotY + SLOT_SIZE) {
                    return inventory.getInventory()[r][c];
                }
            }
        }
        return null;
    }
}
