package dev.creoii.chaos.entity;

import dev.creoii.chaos.ClientGame;

import java.util.UUID;

public class ClientCharacterEntity extends ClientLivingEntity {
    private final String[][] inventory;
    private String[][] lootInventory;

    public ClientCharacterEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale, String classId, String[][] inventory) {
        super(game, uuid, textureId, x, y, scale);
        this.inventory = inventory;
    }

    public String[][] getInventory() {
        return inventory;
    }

    public String[][] getLootInventory() {
        return lootInventory;
    }

    public void setLootInventory(String[][] lootInventory) {
        this.lootInventory = lootInventory;
    }

    public void clearLootInventory() {
        lootInventory = null;
    }
}
