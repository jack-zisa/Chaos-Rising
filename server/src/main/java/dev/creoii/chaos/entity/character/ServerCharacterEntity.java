package dev.creoii.chaos.entity.character;

import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.item.ItemStack;

public class ServerCharacterEntity extends CharacterEntity {
    private final ServerEntityData serverData;

    public ServerCharacterEntity(CharacterEntityType type) {
        super();
        serverData = new ServerEntityData();
    }

    public void setCharacterClass(CharacterClass characterClass) {
        ((CharacterEntityType) type).characterClass().set(characterClass);
        getStats().set(characterClass.baseStatContainer());
        getMaxStats().set(characterClass.baseStatContainer());
    }

    public void dropItem(ItemStack stack) {
        dropItem(stack, false);
    }

    public void dropItem(ItemStack stack, boolean forceDrop) {
        if (lootUuid == null || forceDrop) {
            ServerLootDropEntity lootDropEntity = game.getEntityManager().addEntity(game.getDataManager().getLootDrop("bag"), pos.cpy());
            Inventory inventory = new Inventory(2, 4);
            inventory.addItem(stack);
            lootDropEntity.setInventory(inventory);
            lootUuid = lootDropEntity.getUuid();
        } else {
            ServerLootDropEntity lootDropEntity = (ServerLootDropEntity) game.getEntityManager().getEntity(lootUuid);
            if (lootDropEntity == null || !lootDropEntity.getInventory().addItem(stack))
                dropItem(stack, true);
        }
    }

    /*@Override
    public void postSpawn() {
    }

    @Override
    public void onDeath() {

    }

    @Override
    public EntityController<ServerCharacterEntity> getController() {
        return controller;
    }

    @Override
    public void collisionEnter(ServerEntity other) {
        if (other instanceof ServerLootDropEntity lootDropEntity) {
            game.getMain().getServer().sendToTCP(connectionId, new LootDropOpenS2C(lootDropEntity.getInventory()));
        }
    }

    @Override
    public void collisionExit(ServerEntity other) {
        if (other == null)
            return;

        if (other instanceof ServerLootDropEntity) {
            game.getMain().getServer().sendToTCP(connectionId, new LootDropCloseS2C());
        }
    }*/
}
