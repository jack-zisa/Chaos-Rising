package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.entity.serialization.LootDropData;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

public class LootDropEntity extends Entity {
    private Inventory inventory;

    public LootDropEntity(Game game, EntityType<? extends LootDropEntity> type, int id, Vector2 pos, Inventory inventory) {
        super(game, type, id, pos);
        this.inventory = inventory;
    }

    @Override
    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        super.reinit(id, pos, data);
        inventory = new Inventory(2, 4);
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new LootDropData(getType().id(), inventory.isEmpty() ? null : inventory.getSlots());
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    public void addItem(ItemStack stack) {
        inventory.addItem(stack);
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (gametime - getSpawnTime() >= 2400)
            remove();
    }
}
