package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.input.InputManager;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
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
    private final Predicate<ClientGame> activePredicate;

    protected Slot dragSource;
    protected ItemStack dragStack;

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory, Predicate<ClientGame> activePredicate) {
        super(parent, pos, inventory.getSlots()[0].length * SLOT_SIZE, inventory.getSlots().length * SLOT_SIZE);
        this.inventory = inventory;
        this.activePredicate = activePredicate;
    }

    public InventoryWidget(Screen parent, Vector2 pos, Inventory inventory) {
        this(parent, pos, inventory, main -> true);
    }

    public boolean isActive(ClientGame game) {
        return getInventory() != null && activePredicate.test(game);
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

            for (int r = 0; r < getInventory().getSlots().length; ++r) {
                for (int c = 0; c < getInventory().getSlots()[r].length; ++c) {
                    Slot slot = getInventory().getSlots()[r][c];
                    Sprite sprite = slot.hasItem() ? InventoryScreen.SLOT_SPRITES.get(Slot.Type.NONE) : InventoryScreen.SLOT_SPRITES.get(slot.getType());
                    sprite.setPosition(getPos().x + (c * SLOT_SIZE), getPos().y + (r * SLOT_SIZE));
                    sprite.draw(batch);
                    if (slot.hasItem()) {
                        ItemRenderer.renderItem(renderer.getGame(), batch, slot.getStack().getItem(), new Vector2(getPos().x + (c * SLOT_SIZE) + 3, getPos().y + (r * SLOT_SIZE) + 3), ITEM_SCALE);
                    }
                }
            }

            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                ItemRenderer.renderTooltip(batch, null, mouseOverSlot.getStack().getItem());
            }

            if (dragStack != null && dragStack.getItem() != null) {
                Vector2 mousePos = new Vector2(Gdx.input.getX() - (ITEM_SCALE / 2f), Gdx.graphics.getHeight() - Gdx.input.getY() - (ITEM_SCALE / 2f));
                ItemRenderer.renderItem(renderer.getGame(), batch, dragStack.getItem(), mousePos, ITEM_SCALE);
            }
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getGame()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen && isMouseOver()) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem() && Gdx.input.isTouched()) {
                if (!touched.getStack().clickInSlot(manager.getGame(), manager.getGame().getCharacter().getUuid(), touched)) {
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
                            game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.SWAP, InventoryType.MAIN, InventoryType.MAIN, new SlotEntry(dragSource.getR(), dragSource.getC(), dragSource.hasItem() ? dragSource.getStack().getItem().id() : "", dragSource.hasItem() ? dragSource.getStack().getCount() : 0), new SlotEntry(touched.getR(), touched.getC(), touched.hasItem() ? touched.getStack().getItem().id() : "", touched.hasItem() ? touched.getStack().getCount() : 0)));
                        } else
                            dragSource.setStack(dragStack.copy());
                    } else {
                        game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.MOVE, InventoryType.MAIN, InventoryType.MAIN, new SlotEntry(dragSource.getR(), dragSource.getC(), dragSource.hasItem() ? dragSource.getStack().getItem().id() : "", dragSource.hasItem() ? dragSource.getStack().getCount() : 0), new SlotEntry(touched.getR(), touched.getC(), touched.hasItem() ? touched.getStack().getItem().id() : "", touched.hasItem() ? touched.getStack().getCount() : 0)));

                        if (getInventory().isEmpty()) {
                            if (game.getCharacter().getLootUuid() != null) {
                                game.getCharacter().setLootUuid(null);
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().getUuid()));
                            }
                        }
                    }
                }
            } else {
                game.getClient().sendTCP(new DropSlotItemC2S(game.getCharacter().getUuid(), new SlotEntry(dragSource.getR(), dragSource.getC(), dragSource.hasItem() ? dragSource.getStack().getItem().id() : "", dragSource.hasItem() ? dragSource.getStack().getCount() : 0)));
            }
            dragStack = null;
        }

        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
