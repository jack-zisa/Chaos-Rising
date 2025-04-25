package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.render.screen.widget.InventoryWidget;
import dev.creoii.chaos.render.screen.widget.LootInventoryWidget;
import dev.creoii.chaos.render.screen.widget.Widget;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    public InventoryScreen(Main main, Vector2 pos, Inventory inventory) {
        super(main, "Inventory", pos, (inventory.getSlots().length * 48f) + 31f);

        addWidget("main_inventory", new InventoryWidget(this, pos, inventory));
        addWidget("loot_inventory", new LootInventoryWidget(this, pos.cpy().sub(0f, 400f), main1 -> main.getGame().getActiveCharacter().getLootUuid() != null));
    }

    @Nullable
    public Slot getMouseOverSlot() {
        if (getWidgets().isEmpty())
            return null;
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (Widget widget : getWidgets().values()) {
            if (widget instanceof InventoryWidget inventoryWidget) {
                if (!inventoryWidget.isActive(getMain()))
                    continue;
                Slot slot = inventoryWidget.getSlotAt(mouseX, mouseY);
                if (slot != null)
                    return slot;
            }
        }
        return null;
    }
}
