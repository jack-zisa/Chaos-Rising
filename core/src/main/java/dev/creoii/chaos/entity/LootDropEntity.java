package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;

import java.util.UUID;

public class LootDropEntity extends Entity {
    private final Inventory inventory;

    public LootDropEntity(Game game, UUID uuid, Vector2 pos, float scale, Inventory inventory) {
        super(game, uuid, pos, scale);
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
