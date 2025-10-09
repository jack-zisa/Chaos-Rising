package dev.creoii.chaos.render.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public class CharacterEntityRenderData extends LivingEntityRenderData {
    public Slot[][] slots;

    public CharacterEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, StatContainer statContainer, StatContainer maxStatContainer, Slot[][] slots) {
        super(uuid, EntityGroup.CHARACTER, x, y, xv, yv, textureId, scale, statContainer, maxStatContainer);
        this.slots = slots;
    }

    public Slot[] getHotbar() {
        return slots[0];
    }

    public Slot getWeaponSlot() {
        return getHotbar()[0];
    }

    public Slot getAbilitySlot() {
        return getHotbar()[1];
    }

    public Slot getArmorSlot() {
        return getHotbar()[2];
    }

    public Slot getAccessorySlot() {
        return getHotbar()[3];
    }
}
