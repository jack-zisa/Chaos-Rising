package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.ClientMain;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.c2s.DropSlotItemC2S;
import dev.creoii.chaos.network.packet.c2s.LootDropCloseC2S;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
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
    private final Predicate<ClientMain> activePredicate;

    protected Slot dragSource;
    protected ItemStack dragStack;

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory, Predicate<ClientMain> activePredicate) {
        super(parent, pos, inventory.getSlots()[0].length * SLOT_SIZE, inventory.getSlots().length * SLOT_SIZE);
        this.inventory = inventory;
        this.activePredicate = activePredicate;
    }

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory) {
        this(parent, pos, inventory, main -> true);
    }

    public boolean isActive(ClientMain main) {
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
                if (x >= slotX && x <= slotX + SLOT_SIZE && y >= slotY && y <= slotY + SLOT_SIZE) {
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
                    Sprite sprite = slot.hasItem() ? InventoryScreen.SLOT_SPRITES.get(Slot.Type.NONE) : InventoryScreen.SLOT_SPRITES.get(slot.getType());
                    sprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                    sprite.draw(batch);
                    if (slot.hasItem()) {
                        ItemRenderer.renderItem(renderer.getMain(), batch, slot.getStack().getItem(), new Vector2(getPos().x + (c * SLOT_SIZE) + 3, getPos().y + (r * SLOT_SIZE) + 3), ITEM_SCALE);
                    }
                }
            }

            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                ItemRenderer.renderTooltip(batch, null, mouseOverSlot.getStack().getItem());
            }

            if (dragStack != null && dragStack.getItem() != null) {
                Vector2 mousePos = new Vector2(Gdx.input.getX() - (ITEM_SCALE / 2f), Gdx.graphics.getHeight() - Gdx.input.getY() - (ITEM_SCALE / 2f));
                ItemRenderer.renderItem(renderer.getMain(), batch, dragStack.getItem(), mousePos, ITEM_SCALE);
            }
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen && isMouseOver()) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem() && Gdx.input.isTouched()) {
                if (!touched.getStack().clickInSlot(manager.getMain().getGame(), manager.getMain().getGame().getCharacter().getUuid(), touched)) {
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
        if (!isActive(manager.getMain()))
            return false;
        if (dragStack != null && getParent() instanceof InventoryScreen inventoryScreen) {
            ClientGame game = manager.getMain().getGame();
            Slot touched = inventoryScreen.getMouseOverSlot();
            Inventory mainInventory = ((InventoryWidget) inventoryScreen.getWidget("main_inventory")).inventory;
            if (touched != null) {
                if (!touched.canAccept(dragStack.getItem())) {
                    dragSource.setStack(dragStack.copy());
                } else {
                    if (touched.hasItem()) {
                        if (dragSource.canAccept(touched.getStack().getItem())) {
                            game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.SWAP, getInventory(), mainInventory, dragSource, touched));
                        } else
                            dragSource.setStack(dragStack.copy());
                    } else {
                        game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.MOVE, getInventory(), mainInventory, dragSource, touched));

                        if (getInventory().isEmpty()) {
                            if (game.getCharacter().getLootInventory() != null) {
                                game.getCharacter().clearLootInventory();
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().getUuid()));
                            }
                        }
                    }
                }
            } else {
                game.getClient().sendTCP(new DropSlotItemC2S(game.getCharacter().getUuid(), dragSource));
            }
            dragStack = null;
        }

        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
