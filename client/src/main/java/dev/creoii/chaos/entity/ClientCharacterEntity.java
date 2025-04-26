package dev.creoii.chaos.entity;

import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.inventory.Inventory;

import java.util.UUID;

public class ClientCharacterEntity extends ClientLivingEntity {
    private final Inventory inventory;
    private final String classId;
    private Inventory lootInventory;

    public ClientCharacterEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale, String classId, Inventory inventory) {
        super(game, uuid, textureId, x, y, scale);
        this.inventory = inventory;
        this.classId = classId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getClassId() {
        return classId;
    }

    public Inventory getLootInventory() {
        return lootInventory;
    }

    public void setLootInventory(Inventory lootInventory) {
        this.lootInventory = lootInventory;
    }

    public void clearLootInventory() {
        lootInventory = null;
    }
}
