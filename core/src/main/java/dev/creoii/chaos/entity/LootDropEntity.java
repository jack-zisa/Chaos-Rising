package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;

public class LootDropEntity extends Entity {
    private Inventory inventory;

    public LootDropEntity(LootDropEntityType type) {
        super(type, new Vector2(1, 1), Group.OTHER);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (gametime - getSpawnTime() >= 2400)
            remove();
    }

    @Override
    public EntityController<?> getController() {
        return null;
    }

    @Override
    public void collisionEnter(Entity other) {

    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {
    }

    @Override
    public void remove() {
        super.remove();
        if (game.getActiveCharacter().getLootUuid().equals(uuid))
            game.getActiveCharacter().clearLootUuid();
    }
}
