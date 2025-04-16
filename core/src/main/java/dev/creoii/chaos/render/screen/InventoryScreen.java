package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.inventory.CharacterInventory;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.render.screen.widget.InventoryWidget;
import dev.creoii.chaos.render.screen.widget.Widget;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    public InventoryScreen(Main main, Vector2 pos, Inventory inventory) {
        super(main, "Inventory", pos, (inventory.getSlots().length * 48f) + 31f);
        if (inventory instanceof CharacterInventory characterInventory) {
            addWidget("main_inventory", new InventoryWidget(this, pos, inventory));

            Game game = characterInventory.getCharacter().getGame();
            DataManager dataManager = game.getDataManager();
            Inventory testInventory = new Inventory(2, 4);
            testInventory.addItem(dataManager.getItem("test").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_three").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_multi").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_random").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_spread").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_circle").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_backwards").create(game).getDefaultStack());
            testInventory.addItem(dataManager.getItem("test_two").create(game).getDefaultStack());

            addWidget("loot_inventory", new InventoryWidget(this, pos.cpy().sub(0f, 400f), testInventory, main1 -> main.getGame().getActiveCharacter().getLootUuid() != null));
        }
    }

    @Nullable
    public Slot getMouseOverSlot() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (Widget widget : getWidgets().values()) {
            if (widget instanceof InventoryWidget inventoryWidget) {
                if (!inventoryWidget.getActivePredicate().test(getMain()))
                    continue;
                Slot slot = inventoryWidget.getSlotAt(mouseX, mouseY);
                if (slot != null) return slot;
            }
        }
        return null;
    }
}
