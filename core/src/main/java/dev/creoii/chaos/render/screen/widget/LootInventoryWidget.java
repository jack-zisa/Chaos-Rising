package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.math.Vector2;
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
        super(parent, pos, null, activePredicate);
    }

    @Override
    public Inventory getInventory() {
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            UUID lootUuid = inventoryScreen.getMain().getGame().getActiveCharacter().getLootUuid();
            if (lootUuid == null)
                return null;
            Entity entity = inventoryScreen.getMain().getGame().getEntityManager().getEntity(lootUuid);
            if (entity instanceof LootDropEntity loot) {
                return loot.getInventory();
            }
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
                dragSource = touched;
                dragStack = touched.getStack();
                touched.setStack(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            UUID lootUuid = inventoryScreen.getMain().getGame().getActiveCharacter().getLootUuid();
            if (lootUuid != null) {
                Entity entity = inventoryScreen.getMain().getGame().getEntityManager().getEntity(lootUuid);
                if (entity instanceof LootDropEntity lootDropEntity && lootDropEntity.getInventory().isEmpty()) {
                    inventoryScreen.getMain().getGame().getEntityManager().removeEntity(lootUuid);
                    inventoryScreen.getMain().getGame().getActiveCharacter().clearLootUuid();
                }
            }
        }
        return super.touchUp(manager, screenX, screenY, pointer, button);
    }
}
