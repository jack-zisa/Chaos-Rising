package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.EntityGroup;

public class LootDropEntityRenderData extends EntityRenderData {
    public Slot[][] slots;

    public LootDropEntityRenderData(int id, float x, float y, float xv, float yv, String textureId, float scale, Slot[][] slots) {
        super(id, EntityGroup.LOOT_DROP, x, y, xv, yv, textureId, scale);
        this.slots = slots;
    }
}
