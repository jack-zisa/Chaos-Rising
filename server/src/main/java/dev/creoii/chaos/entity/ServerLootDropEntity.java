package dev.creoii.chaos.entity;

import dev.creoii.chaos.entity.behavior.EntityController;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.EntityGroup;

public class ServerLootDropEntity extends ServerEntity {
    private Inventory inventory;

    public ServerLootDropEntity(LootDropEntityType type) {
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
    public void collisionEnter(ServerEntity other) {

    }

    @Override
    public void collisionExit(ServerEntity other) {

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
