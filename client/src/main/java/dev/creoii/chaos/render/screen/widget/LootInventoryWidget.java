package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.ClientMain;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.packet.c2s.LootDropCloseC2S;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<ClientMain> activePredicate) {
        super(parent, pos, new Inventory(2, 4), activePredicate);
    }

    @Override
    public Inventory getInventory() {
        return getParent().getMain().getGame().getCharacter().getLootInventory();
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem()) {
                if (!touched.getStack().clickInSlot(manager.getMain().getGame(), manager.getMain().getGame().getCharacter().getUuid(), touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        ClientMain main = manager.getMain();
                        ClientGame game = main.getGame();
                        Inventory mainInventory = ((InventoryWidget) inventoryScreen.getWidget("main_inventory")).getInventory();
                        game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.QUICK_MOVE, getInventory(), mainInventory, dragSource, touched));

                        if (getInventory().isEmpty()) {
                            if (game.getCharacter().getLootInventory() != null) {
                                game.getCharacter().clearLootInventory();
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
