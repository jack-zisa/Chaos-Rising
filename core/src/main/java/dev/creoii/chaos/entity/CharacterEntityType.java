package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
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

    public CharacterEntity create(Game game, Vector2 pos, Map<String, Object> customData) {
        CharacterEntity character = new CharacterEntity(this);
        character.game = game;
        character.uuid = UUID.randomUUID();
        character.pos = pos;
        character.centerPos = new Vector2();
        character.colliderRect = new Rectangle();
        character.colliderRect.setPosition(pos);
        character.colliderRect.setWidth(character.getCollider().x * scale());
        character.colliderRect.setHeight(character.getCollider().y * scale());
        character.collidingWith = new HashSet<>();
        character.spawnTime = game.getGametime();
        if (characterClass.get().textureId() != null) {
            character.sprite = new Sprite(game.getTextureManager().getTexture("class", characterClass.get().textureId()));
            character.sprite.setSize(scale(), scale());
            character.getCenterPos();
        }
        character.postSpawn();
        return character;
    }
}
