package dev.creoii.chaos.client.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.c2s.DropSlotItemC2S;
import dev.creoii.chaos.network.c2s.SlotUpdateC2S;
import dev.creoii.chaos.client.render.ItemRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.screen.InventoryScreen;
import dev.creoii.chaos.client.render.screen.Screen;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class InventoryWidget extends Widget {
    public static final float SLOT_SIZE = 49f;
    private static final float ITEM_SCALE = 42f;
    private final Slot[][] slots;
    private final Predicate<ClientGame> activePredicate;

    protected Slot dragSource;
    protected ItemStack dragStack;

    public InventoryWidget(Screen parent, Vector2 pos, Slot[][] slots, Predicate<ClientGame> activePredicate) {
        super(parent, pos, slots.length * SLOT_SIZE, slots.length * SLOT_SIZE);
        this.slots = slots;
        this.activePredicate = activePredicate;
    }

    public InventoryWidget(Screen parent, Vector2 pos, Slot[][] slots) {
        this(parent, pos, slots, _ -> true);
    }

    public boolean isActive(ClientGame game) {
        return getInventory() != null && activePredicate.test(game);
    }

    public Slot[][] getInventory() {
        return slots;
    }

    @Nullable
    public Slot getSlotAt(float x, float y) {
        for (int r = 0; r < getInventory().length; ++r) {
            for (int c = 0; c < getInventory()[r].length; ++c) {
                float slotX = getPos().x + (c * SLOT_SIZE);
                float slotY = getPos().y + (r * SLOT_SIZE);
                if (x >= slotX && x <= slotX + SLOT_SIZE && y >= slotY && y <= slotY + SLOT_SIZE) {
                    return getInventory()[r][c];
                }
            }
        }
        return null;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        if (!isActive(renderer.getGame()))
            return;

        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot mouseOverSlot = inventoryScreen.getMouseOverSlot();
            if (batch == null && shapeRenderer != null) {
                if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                    ItemRenderer.renderTooltip(null, shapeRenderer, mouseOverSlot.getStack().getItem());
                }
                return;
            }

            for (int r = 0; r < getInventory().length; ++r) {
                for (int c = 0; c < getInventory()[r].length; ++c) {
                    Slot slot = getInventory()[r][c];
                    Sprite sprite = slot.hasItem() ? InventoryScreen.SLOT_SPRITES.get(Slot.Type.NONE) : InventoryScreen.SLOT_SPRITES.get(slot.getType());
                    sprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                    sprite.draw(batch);
                    if (slot.hasItem()) {
                        ItemRenderer.renderItem(renderer.getGame(), batch, slot.getStack().getItem().id(), new Vector2(getPos().x + (c * SLOT_SIZE) + 3, getPos().y + (r * SLOT_SIZE) + 3), ITEM_SCALE);
                    }
                }
            }

            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                ItemRenderer.renderTooltip(batch, null, mouseOverSlot.getStack().getItem());
            }

            if (dragStack != null && dragStack != ItemStack.EMPTY) {
                Vector2 mousePos = new Vector2(Gdx.input.getX() - (ITEM_SCALE / 2f), Gdx.graphics.getHeight() - Gdx.input.getY() - (ITEM_SCALE / 2f));
                ItemRenderer.renderItem(renderer.getGame(), batch, dragStack.getItem().id(), mousePos, ITEM_SCALE);
            }
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getGame()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen && isMouseOver()) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem()) {
                if (!touched.getStack().clickInSlot(manager.getGame(), manager.getGame().getCharacter().id, touched)) {
                    dragSource = touched;
                    dragStack = touched.takeStack();
                }
                return true;
            }
        }
        return super.touchDown(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getGame()))
            return false;
        if (dragStack != null && getParent() instanceof InventoryScreen inventoryScreen) {
            ClientGame game = manager.getGame();
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null) {
                if (!touched.canAccept(dragStack.getItem())) {
                    dragSource.setStack(dragStack.copy());
                } else {
                    if (touched.hasItem()) {
                        if (dragSource.canAccept(touched.getStack().getItem())) {
                            game.getClient().sendTCP(new SlotUpdateC2S(manager.getGame().getCharacter().id, SlotUpdateC2S.Action.SWAP, InventoryType.MAIN, InventoryType.MAIN, dragSource, touched));
                        } else
                            dragSource.setStack(dragStack.copy());
                    } else {
                        game.getClient().sendTCP(new SlotUpdateC2S(manager.getGame().getCharacter().id, SlotUpdateC2S.Action.MOVE, InventoryType.MAIN, InventoryType.MAIN, dragSource, touched));

                        /*if (getInventory().isEmpty()) {
                            if (game.getCharacter().getLootUuid() != null) {
                                game.getCharacter().setLootUuid(null);
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().uuid()));
                            }
                        }*/
                    }
                }
            } else {
                game.getClient().sendTCP(new DropSlotItemC2S(manager.getGame().getCharacter().id, dragSource));
            }
            dragStack = null;
        }

        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
