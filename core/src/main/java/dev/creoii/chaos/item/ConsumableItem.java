package dev.creoii.chaos.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class ConsumableItem extends Item {
    public ConsumableItem(Rarity rarity, String textureId) {
        super(Type.CONSUMABLE, rarity, textureId, StatModifier.NONE);
    }

    @Override
    public boolean clickInSlot(InputManager manager, Slot slot, ItemStack stack) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            slot.setStack(ItemStack.EMPTY);
            return true;
        }
        return super.clickInSlot(manager, slot, stack);
    }
}
