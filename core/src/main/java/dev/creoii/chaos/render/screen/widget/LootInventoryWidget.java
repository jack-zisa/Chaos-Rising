package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import java.util.UUID;
import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<Main> activePredicate) {
        super(parent, pos, new Inventory(2, 4), activePredicate);
    }

    @Override
    public Inventory getInventory() {
        Game game = getParent().getMain().getGame();
        UUID lootUuid = game.getActiveCharacter().getLootUuid();
        if (lootUuid == null)
            return null;
        Entity entity = game.getEntityManager().getEntity(lootUuid);
        if (entity instanceof LootDropEntity loot) {
            return loot.getInventory();
        }
        return null;
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem()) {
                if (!touched.getStack().clickInSlot(manager, touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        Game game = manager.getMain().getGame();
                        Inventory main = ((InventoryWidget) inventoryScreen.getWidget("main_inventory")).getInventory();

                        getInventory().onRemoveItemFromSlot(touched, touched.getStack());
                        main.addItem(touched.takeStack());

                        if (getInventory().isEmpty()) {
                            LootDropEntity lootDropEntity = (LootDropEntity) game.getEntityManager().getEntity(game.getActiveCharacter().getLootUuid());
                            if (lootDropEntity != null) {
                                game.getEntityManager().removeEntity(lootDropEntity);
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
