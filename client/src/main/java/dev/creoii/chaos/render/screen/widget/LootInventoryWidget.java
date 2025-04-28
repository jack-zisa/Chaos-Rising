package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.input.InputManager;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.packet.c2s.LootDropCloseC2S;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<ClientGame> activePredicate) {
        super(parent, pos, new Inventory(2, 4), activePredicate);
    }

    @Override
    @Nullable
    public Inventory getInventory() {
        UUID lootUuid = getParent().getGame().getCharacter().getLootUuid();
        if (lootUuid == null)
            return null;
        return ((LootDropEntity) getParent().getGame().getEntityManager().getEntity(lootUuid)).getInventory();
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getGame()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem()) {
                if (!touched.getStack().clickInSlot(manager.getGame(), manager.getGame().getCharacter().getUuid(), touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        ClientGame game = manager.getGame();
                        Inventory mainInventory = ((InventoryWidget) inventoryScreen.getWidget("main_inventory")).getInventory();
                        game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.QUICK_MOVE, getInventory(), mainInventory, dragSource, touched));

                        if (getInventory() != null && getInventory().isEmpty()) {
                            if (game.getCharacter().getLootUuid() != null) {
                                game.getCharacter().setLootUuid(null);
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().getUuid()));
                            }
                        }
                        return true;
                    }
                    dragSource = touched;
                    dragStack = touched.takeStack();
                }
                return true;
            }
        }
        return false;
    }
}
