package dev.creoii.chaos.client.render.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.client.render.screen.widget.InventoryWidget;
import dev.creoii.chaos.client.render.screen.widget.Widget;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import javax.annotation.Nullable;

public class InventoryScreen extends Screen {
    public static final Int2ObjectOpenHashMap<Sprite> SLOT_SPRITES = new Int2ObjectOpenHashMap<>();

    public InventoryScreen(ClientGame game, Vector2 pos, Slot[][] slots) {
        super(game, "Inventory", pos, (slots.length * 48f) + 31f);

        addWidget("main_inventory", new InventoryWidget(this, pos, slots));
        //addWidget("loot_inventory", new LootInventoryWidget(this, pos.cpy().sub(0f, 400f), game1 -> game1.getCharacter().getLootUuid() != null));

        SLOT_SPRITES.put(Slot.Type.NONE.ordinal(), new Sprite(new Texture("textures/ui/slot.png")));
        SLOT_SPRITES.put(Slot.Type.WEAPON.ordinal(), new Sprite(new Texture("textures/ui/weapon_slot.png")));
        SLOT_SPRITES.put(Slot.Type.ABILITY.ordinal(), new Sprite(new Texture("textures/ui/ability_slot.png")));
        SLOT_SPRITES.put(Slot.Type.ARMOR.ordinal(), new Sprite(new Texture("textures/ui/armor_slot.png")));
        SLOT_SPRITES.put(Slot.Type.ACCESSORY.ordinal(), new Sprite(new Texture("textures/ui/accessory_slot.png")));

        SLOT_SPRITES.forEach((_, sprite) -> {
            sprite.setSize(InventoryWidget.SLOT_SIZE, InventoryWidget.SLOT_SIZE);
        });
    }

    @Nullable
    public Slot getMouseOverSlot() {
        if (getWidgets().isEmpty())
            return null;

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (Widget widget : getWidgets().values()) {
            if (widget instanceof InventoryWidget inventoryWidget) {
                if (!inventoryWidget.isActive(getGame()))
                    continue;
                Slot slot = inventoryWidget.getSlotAt(mouseX, mouseY);
                if (slot != null)
                    return slot;
            }
        }
        return null;
    }
}
