package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.Mutable;

import java.util.Map;
import java.util.UUID;

public record CharacterEntityType(Mutable<CharacterClass> characterClass) implements EntityType<CharacterEntity> {
    @Override
    public String id() {
        return "";
    }

    @Override
    public float scale() {
        return characterClass.get().scale();
    }

    @Override
    public String textureId() {
        return characterClass.get().textureId();
    }

    public CharacterEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        CharacterEntity character = new CharacterEntity(game, uuid, pos, characterClass, (int) customData.get("connection_id"), new Inventory(3, 4));
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
