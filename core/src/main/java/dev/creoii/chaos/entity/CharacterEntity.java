package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.Mutable;

import javax.annotation.Nullable;
import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private final int connectionId;
    private final Mutable<CharacterClass> characterClass;
    private final Inventory inventory;
    @Nullable
    private UUID lootUuid;

    public CharacterEntity(Game game, EntityType<? extends CharacterEntity> type, UUID uuid, Vector2 pos, int connectionId, Mutable<CharacterClass> characterClass, Inventory inventory) {
        super(game, type, uuid, pos);
        this.connectionId = connectionId;
        this.characterClass = characterClass;
        this.inventory = inventory;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Mutable<CharacterClass> getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass.set(characterClass);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Nullable
    public UUID getLootUuid() {
        return lootUuid;
    }

    public void setLootUuid(@Nullable UUID lootUuid) {
        this.lootUuid = lootUuid;
    }
}
