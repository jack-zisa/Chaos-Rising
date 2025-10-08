package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.input.InputManager;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
import dev.creoii.chaos.render.entity.data.SlotRenderData;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<ClientGame> activePredicate) {
        super(parent, pos, new SlotRenderData[2][4], activePredicate);
    }

    @Override
    @Nullable
    public SlotRenderData[][] getInventory() {
        /*UUID lootUuid = getParent().getGame().getCharacter().getLootUuid();
        if (lootUuid == null)
            return null;
        return ((LootDropEntity) getParent().getGame().getEntityManager().getEntityData(lootUuid)).getInventory();*/
        return null;
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getGame()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            SlotRenderData touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.stack.getCount() > 0) {
                ClientGame game = manager.getGame();
                /*if (!touched.getStack().clickInSlot(manager.getGame(), manager.getGame().getCharacter().uuid, touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        game.getClient().sendTCP(new SlotUpdateC2S(manager.getGame().getCharacter().uuid, SlotUpdateC2S.Action.QUICK_MOVE, InventoryType.LOOT, InventoryType.MAIN, new SlotEntry(dragSource.getR(), dragSource.getC(), dragSource.getStack().getItem().id(), dragSource.getStack().getCount()), new SlotEntry(touched.getR(), touched.getC(), touched.getStack().getItem().id(), touched.getStack().getCount())));

                        if (getInventory() != null && getInventory().isEmpty()) {
                            if (game.getCharacter().getLootUuid() != null) {
                                game.getCharacter().setLootUuid(null);
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().uuid()));
                            }
                        }
                        return true;
                    }
                    dragSource = touched;
                    dragStack = touched.takeStack();
                }*/
                return true;
            }
        }
        return false;
    }
}
