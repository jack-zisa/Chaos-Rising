package dev.creoii.chaos.entity;

import dev.creoii.chaos.ClientGame;

import java.util.UUID;

public class ClientLootDropEntity extends ClientEntity {
    private final String[][] inventory;

    public ClientLootDropEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale, String[][] inventory) {
        super(game, uuid, textureId, x, y, scale);
        this.inventory = inventory;
    }

    public String[][] getInventory() {
        return inventory;
    }
}
