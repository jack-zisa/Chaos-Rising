package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.Mutable;

import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private final Mutable<CharacterClass> characterClass;
    private final int connectionId;
    private final Inventory inventory;
    private Inventory lootInventory;

    public CharacterEntity(Game game, UUID uuid, Vector2 pos, Mutable<CharacterClass> characterClass, int connectionId, Inventory inventory) {
        super(game, uuid, pos, characterClass.get().scale());
        this.characterClass = characterClass;
        this.connectionId = connectionId;
        this.inventory = inventory;
    }

    public Mutable<CharacterClass> getCharacterClass() {
        return characterClass;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Inventory getInventory() {
        return inventory;
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
