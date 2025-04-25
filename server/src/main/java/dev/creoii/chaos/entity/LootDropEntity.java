package dev.creoii.chaos.entity;

import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.network.packet.util.EntityGroup;

public class LootDropEntity extends Entity {
    private Inventory inventory;

    public LootDropEntity(LootDropEntityType type) {
        super(type, EntityGroup.OTHER);
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
        if (game.getEntityManager().getCharacter(uuid).getLootUuid().equals(uuid))
            game.getEntityManager().getCharacter(uuid).clearLootUuid();
    }
}
