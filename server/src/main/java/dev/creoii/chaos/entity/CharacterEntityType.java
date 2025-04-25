package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.character.CharacterClass;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.util.Mutable;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record CharacterEntityType(Mutable<CharacterClass> characterClass) implements EntityType<CharacterEntity> {
    @Override
    public String id() {
        return "";
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < characterClass.get().scale())
            main.getGame().getCollisionManager().setCellSize(characterClass.get().scale());
    }

    @Override
    public float scale() {
        return characterClass.get().scale();
    }

    @Override
    public String textureId() {
        return characterClass.get().textureId();
    }

    public CharacterEntity create(ServerGame game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        CharacterEntity character = new CharacterEntity(this);
        character.game = game;
        character.uuid = uuid;
        character.pos = pos;
        character.centerPos = new Vector2();
        character.colliderRect = new Rectangle();
        character.colliderRect.setPosition(pos);
        character.colliderRect.setSize(scale());
        character.collidingWith = new HashSet<>();
        character.spawnTime = game.getGametime();
        character.getCenterPos();
        character.postSpawn();
        return character;
    }
}
