package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;

import java.util.Map;
import java.util.UUID;

public record CharacterEntityType(Mutable<CharacterClass> characterClass) implements EntityType<CharacterEntity> {
    @Override
    public String id() {
        return characterClass.get().id();
    }

    @Override
    public float scale() {
        return characterClass.get().scale() * Entity.COORDINATE_SCALE;
    }

    @Override
    public EntityGroup group() {
        return EntityGroup.CHARACTER;
    }

    public CharacterEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        CharacterEntity character = new CharacterEntity(game, this, uuid, pos.cpy(), (int) customData.get("connection_id"), (CharacterInventory) customData.get("inventory"));
/*        character.centerPos = new Vector2();
        character.colliderRect = new Rectangle();
        character.colliderRect.setPosition(pos);
        character.colliderRect.setSize(scale());
        character.collidingWith = new HashSet<>();
        character.spawnTime = game.getGametime();
        character.getCenterPos();
        character.postSpawn();*/
        return character;
    }
}
