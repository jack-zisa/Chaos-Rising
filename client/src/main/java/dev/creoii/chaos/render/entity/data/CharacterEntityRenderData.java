package dev.creoii.chaos.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public class CharacterEntityRenderData extends LivingEntityRenderData {
    public SlotRenderData[][] slots;

    public CharacterEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, StatContainer statContainer, StatContainer maxStatContainer, SlotRenderData[][] slots) {
        super(uuid, EntityGroup.CHARACTER, x, y, xv, yv, textureId, scale, statContainer, maxStatContainer);
        this.slots = slots;    }

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
