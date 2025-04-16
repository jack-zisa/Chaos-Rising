package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
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
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class InventoryWidget extends Widget {
    public static final float SLOT_SIZE = 49f;
    private static final float ITEM_SCALE = 42f;
    private final Inventory inventory;
    private final Predicate<Main> renderPredicate;

    private Slot dragSource;
    private ItemStack dragStack;

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory, Predicate<Main> renderPredicate) {
        super(parent, pos);
        this.inventory = inventory;
        this.renderPredicate = renderPredicate;
    }

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory) {
        this(parent, pos, inventory, main -> true);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Slot getSlotAt(float x, float y) {
        for (int r = 0; r < inventory.getSlots().length; ++r) {
            for (int c = 0; c < inventory.getSlots()[r].length; ++c) {
                float slotX = getPos().x + (c * SLOT_SIZE);
                float slotY = getPos().y + (r * SLOT_SIZE);
                if (x >= slotX && x <= slotX + SLOT_SIZE &&
                    y >= slotY && y <= slotY + SLOT_SIZE) {
                    return inventory.getSlots()[r][c];
                }
            }
        }
        return null;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (!renderPredicate.test(renderer.getMain()))
            return;

        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot mouseOverSlot = inventoryScreen.getMouseOverSlot();
            if (batch == null && shapeRenderer != null) {
                if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                    ItemRenderer.renderTooltip(null, shapeRenderer, mouseOverSlot.getStack().getItem());
                }
                return;
            }

            for (int r = 0; r < inventory.getSlots().length; ++r) {
                for (int c = 0; c < inventory.getSlots()[r].length; ++c) {
                    Slot slot = inventory.getSlots()[r][c];
                    if (slot == null)
                        continue;
                    Sprite sprite = slot.hasItem() ? Slot.Type.NONE.getSprite() : slot.getType().getSprite();
                    sprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                    sprite.draw(batch);
                    if (slot.hasItem()) {
                        ItemRenderer.renderItem(batch, slot.getStack().getItem(), new Vector2(getPos().x + (c * SLOT_SIZE) + 3, getPos().y + (r * SLOT_SIZE) + 3), ITEM_SCALE);
                    }
                }
            }

            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                ItemRenderer.renderTooltip(batch, null, mouseOverSlot.getStack().getItem());
            }

            if (dragStack != null && dragStack.getItem() != null) {
                Vector2 mousePos = new Vector2(Gdx.input.getX() - (ITEM_SCALE / 2f), Gdx.graphics.getHeight() - Gdx.input.getY() - (ITEM_SCALE / 2f));
                ItemRenderer.renderItem(batch, dragStack.getItem(), mousePos, ITEM_SCALE);
            }
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem() && Gdx.input.isTouched()) {
                dragSource = touched;
                dragStack = touched.getStack();
                touched.setStack(null);
            }
        }
        return super.touchDown(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (dragStack != null && getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null) {
                if (!touched.canAccept(dragStack.getItem())) {
                    dragSource.setStack(dragStack);
                } else {
                    if (touched.hasItem()) {
                        if (dragSource.canAccept(touched.getStack().getItem())) {
                            inventory.onRemoveItemFromSlot(dragSource, dragStack);
                            ItemStack temp = touched.getStack().copy();
                            inventory.onRemoveItemFromSlot(touched, temp);
                            touched.setStack(dragStack);
                            inventory.onAddItemToSlot(touched, dragStack);
                            dragSource.setStack(temp);
                            inventory.onAddItemToSlot(dragSource, temp);
                        } else {
                            dragSource.setStack(dragStack);
                        }
                    } else {
                        inventory.onRemoveItemFromSlot(dragSource, dragStack);
                        touched.setStack(dragStack);
                        inventory.onAddItemToSlot(touched, dragStack);
                    }
                }
            } else {
                dragSource.setStack(dragStack);
            }
            dragStack = null;
        }

        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
