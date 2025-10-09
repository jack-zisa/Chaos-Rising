package dev.creoii.chaos.render.data;

import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class LootDropEntityRenderData extends EntityRenderData {
    public SlotRenderData[][] slots;

    public LootDropEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, SlotRenderData[][] slots) {
        super(uuid, EntityGroup.LOOT_DROP, x, y, xv, yv, textureId, scale);
        this.slots = slots;
    }
}
