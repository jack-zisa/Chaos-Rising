package dev.creoii.chaos.render.entity.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class CharacterEntityRenderData extends LivingEntityRenderData {
    public SlotRenderData[][] slots;

    public CharacterEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, SlotRenderData[][] slots) {
        super(uuid, EntityGroup.CHARACTER, x, y, xv, yv, textureId, scale);
        this.slots = slots;

        for (int r = 0; r < slots.length; ++r) {
            for (int c = 0; c < slots[r].length; ++c) {
                slots[r][c] = new SlotRenderData(ItemStack.EMPTY, Slot.Type.NONE);
            }
        }

        getWeaponSlot().type = Slot.Type.WEAPON;
        getAbilitySlot().type = Slot.Type.ABILITY;
        getArmorSlot().type = Slot.Type.ARMOR;
        getAccessorySlot().type = Slot.Type.ACCESSORY;
    }

    public SlotRenderData[] getHotbar() {
        return slots[slots.length - 1];
    }

    public SlotRenderData getWeaponSlot() {
        return getHotbar()[0];
    }

    public SlotRenderData getAbilitySlot() {
        return getHotbar()[1];
    }

    public SlotRenderData getArmorSlot() {
        return getHotbar()[2];
    }

    public SlotRenderData getAccessorySlot() {
        return getHotbar()[3];
    }
}
