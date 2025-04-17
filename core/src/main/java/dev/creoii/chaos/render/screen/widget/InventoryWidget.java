package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.LootDropEntity;
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
    private final Predicate<Main> activePredicate;

    protected Slot dragSource;
    protected ItemStack dragStack;

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory, Predicate<Main> activePredicate) {
        super(parent, pos);
        this.inventory = inventory;
        this.activePredicate = activePredicate;
    }

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory) {
        this(parent, pos, inventory, main -> true);
    }

    public boolean isActive(Main main) {
        return getInventory() != null && activePredicate.test(main);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Slot getSlotAt(float x, float y) {
        for (int r = 0; r < getInventory().getSlots().length; ++r) {
            for (int c = 0; c < getInventory().getSlots()[r].length; ++c) {
                float slotX = getPos().x + (c * SLOT_SIZE);
                float slotY = getPos().y + (r * SLOT_SIZE);
                if (x >= slotX && x <= slotX + SLOT_SIZE &&
                    y >= slotY && y <= slotY + SLOT_SIZE) {
                    return getInventory().getSlots()[r][c];
                }
            }
        }
        return null;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (!isActive(renderer.getMain()))
            return;

        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot mouseOverSlot = inventoryScreen.getMouseOverSlot();
            if (batch == null && shapeRenderer != null) {
                if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                    ItemRenderer.renderTooltip(null, shapeRenderer, mouseOverSlot.getStack().getItem());
                }
                return;
            }

            for (int r = 0; r < getInventory().getSlots().length; ++r) {
                for (int c = 0; c < getInventory().getSlots()[r].length; ++c) {
                    Slot slot = getInventory().getSlots()[r][c];
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
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem() && Gdx.input.isTouched()) {
                ItemStack stack = touched.getStack();
                if (stack == null || stack.getItem() == null)
                    return false;

                if (stack.clickInSlot(manager, touched)) {
                    return true;
                }

                dragSource = touched;
                dragStack = stack;
                touched.setStack(null);
                return true;
            }
        }
        return super.touchDown(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (dragStack != null && getParent() instanceof InventoryScreen inventoryScreen) {
            Game game = manager.getMain().getGame();
            Inventory main = ((InventoryWidget) inventoryScreen.getWidget("main_inventory")).inventory;
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null) {
                if (!touched.canAccept(dragStack.getItem())) {
                    dragSource.setStack(dragStack.copy());
                } else {
                    if (touched.hasItem()) {
                        if (dragSource.canAccept(touched.getStack().getItem())) {
                            getInventory().onRemoveItemFromSlot(dragSource, dragStack);
                            ItemStack temp = touched.getStack().copy();
                            main.onRemoveItemFromSlot(touched, temp);
                            touched.setStack(dragStack.copy());
                            getInventory().onAddItemToSlot(touched, touched.getStack());
                            dragSource.setStack(temp);
                            main.onAddItemToSlot(dragSource, temp);
                        } else dragSource.setStack(dragStack.copy());
                    } else {
                        getInventory().onRemoveItemFromSlot(dragSource, dragStack);
                        touched.setStack(dragStack.copy());
                        main.onAddItemToSlot(touched, touched.getStack());

                        if (getInventory().isEmpty()) {
                            LootDropEntity lootDropEntity = (LootDropEntity) game.getEntityManager().getEntity(game.getActiveCharacter().getLootUuid());
                            if (lootDropEntity != null) {
                                game.getEntityManager().removeEntity(lootDropEntity);
                            }
                        }
                    }
                }
            } else {
                game.getActiveCharacter().dropItem(dragStack.copy(), false);
                main.onRemoveItemFromSlot(dragSource, dragStack);
            }
            dragStack = null;
        }

        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
