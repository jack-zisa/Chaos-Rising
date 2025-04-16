package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.inventory.CharacterInventory;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.render.screen.widget.InventoryWidget;

public class InventoryScreen extends Screen {
    public InventoryScreen(Vector2 pos, Inventory inventory) {
        super("Inventory", pos, (inventory.getSlots().length * 48f) + 31f);
        if (inventory instanceof CharacterInventory characterInventory) {
            addWidget("main_inventory", new InventoryWidget(pos, inventory));

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
            addWidget("loot_inventory", new InventoryWidget(pos.cpy().sub(0f, 400f), testInventory, main -> main.getGame().getActiveCharacter().getLootDrop() != null));
        }
    }
}
