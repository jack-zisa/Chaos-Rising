package dev.creoii.chaos.render.entity.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class LootDropEntityRenderData extends EntityRenderData {
    public SlotRenderData[][] slots;

    public LootDropEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, SlotRenderData[][] slots) {
        super(uuid, EntityGroup.LOOT_DROP, x, y, xv, yv, textureId, scale);
        this.slots = slots;

        for (int r = 0; r < slots.length; ++r) {
            for (int c = 0; c < slots[r].length; ++c) {
                slots[r][c] = new SlotRenderData(ItemStack.EMPTY, Slot.Type.NONE);
            }
        }
    }
}
