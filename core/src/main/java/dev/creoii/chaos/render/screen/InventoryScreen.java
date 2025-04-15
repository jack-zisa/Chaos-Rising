package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.render.ItemRenderer;
import dev.creoii.chaos.render.Renderer;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    private static final float SLOT_SIZE = 49f;
    private static final float ITEM_SCALE = 42f;
    private final Inventory inventory;
    private final Sprite slotSprite;

    private Slot dragSource;
    private ItemStack dragStack;

    public InventoryScreen(Vector2 pos, Inventory inventory) {
        super("Inventory", pos, (inventory.getSlots().length * 48f) + 31f);
        this.inventory = inventory;
        slotSprite = new Sprite(new Texture("textures/ui/slot.png"));
        slotSprite.setSize(SLOT_SIZE, SLOT_SIZE);
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        super.render(renderer, batch, shapeRenderer, font, debug);

        if (batch == null && shapeRenderer != null) {
            shapeRenderer.setColor(BACKGROUND_OVERLAY);
            shapeRenderer.rect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
            return;
        }

        for (int r = 0; r < inventory.getSlots().length; ++r) {
            for (int c = 0; c < inventory.getSlots()[r].length; ++c) {
                slotSprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                slotSprite.draw(batch);
                Slot slot = inventory.getSlots()[r][c];
                if (slot != null && slot.hasItem()) {
                    ItemRenderer.renderItem(batch, slot.getStack().getItem(), new Vector2(getPos().x + (c * SLOT_SIZE) + 3, getPos().y + (r * SLOT_SIZE) + 3), ITEM_SCALE);
                }
            }
        }

        Slot mouseOverSlot = getMouseOverSlot();
        if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
            ItemRenderer.renderTooltip(batch, font, mouseOverSlot.getStack().getItem());
        }

        if (dragStack != null && dragStack.getItem() != null) {
            Vector2 mousePos = new Vector2(Gdx.input.getX() - (ITEM_SCALE / 2f), Gdx.graphics.getHeight() - Gdx.input.getY() - (ITEM_SCALE / 2f));
            ItemRenderer.renderItem(batch, dragStack.getItem(), mousePos, ITEM_SCALE);
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        Slot touched = getMouseOverSlot();
        if (touched != null && touched.hasItem() && Gdx.input.isTouched()) {
            dragSource = touched;
            dragStack = touched.getStack();
            touched.setStack(null);
        }
        return super.touchDown(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (dragStack != null) {
            Slot touched = getMouseOverSlot();
            if (touched != null) {
                if (touched.hasItem()) {
                    dragSource.setStack(dragStack);
                    inventory.swap(dragSource.getX(), dragSource.getY(), touched.getX(), touched.getY());
                } else touched.setStack(dragStack);
            } else dragSource.setStack(dragStack);
            dragStack = null;
        }
        return super.touchUp(manager, screenX, screenY, pointer, button);
    }

    @Nullable
    public Slot getMouseOverSlot() {
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (int r = 0; r < inventory.getSlots().length; ++r) {
            for (int c = 0; c < inventory.getSlots()[r].length; ++c) {
                float slotX = getPos().x + (c * SLOT_SIZE);
                float slotY = getPos().y + (r * SLOT_SIZE);

                if (Gdx.input.getX() >= slotX && Gdx.input.getX() <= slotX + SLOT_SIZE && mouseY >= slotY && mouseY <= slotY + SLOT_SIZE) {
                    return inventory.getSlots()[r][c];
                }
            }
        }
        return null;
    }
}
