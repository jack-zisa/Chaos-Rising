package dev.creoii.chaos.client.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.c2s.SlotUpdateC2S;
import dev.creoii.chaos.client.render.screen.InventoryScreen;
import dev.creoii.chaos.client.render.screen.Screen;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<ClientGame> activePredicate) {
        super(parent, pos, new Slot[2][4], activePredicate);
    }

    @Override
    @Nullable
    public Slot[][] getInventory() {
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
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.getStack().getCount() > 0) {
                ClientGame game = manager.getGame();
                if (!touched.getStack().clickInSlot(manager.getGame(), manager.getGame().getCharacter().uuid, touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        game.getClient().sendTCP(new SlotUpdateC2S(manager.getGame().getCharacter().uuid, SlotUpdateC2S.Action.QUICK_MOVE, InventoryType.LOOT, InventoryType.MAIN, dragSource, touched));

                        /*if (getInventory() != null && getInventory().isEmpty()) {
                            if (game.getCharacter().getLootUuid() != null) {
                                game.getCharacter().setLootUuid(null);
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().uuid()));
                            }
                        }*/
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
