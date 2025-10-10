package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;

import java.util.Map;

public record CharacterEntityType(Mutable<CharacterClass> characterClass) implements EntityType<CharacterEntity> {
    public static final MapCodec<CharacterEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            CharacterClass.ID_CODEC.fieldOf("class").forGetter(characterEntityType -> characterEntityType.characterClass().get())
        ).apply(instance, characterClass -> new CharacterEntityType(new Mutable<>(characterClass)));
    });

    @Override
    public String id() {
        return characterClass.get().id();
    }

    @Override
    public EntityGroup group() {
        return EntityGroup.CHARACTER;
    }

    @Override
    public float scale() {
        return characterClass.get().scale() * Entity.COORDINATE_SCALE;
    }

    public CharacterEntity create(Game game, int id, Vector2 pos, Map<String, Object> customData) {
        CharacterEntity character = new CharacterEntity(game, this, id, pos.cpy(), (int) customData.get("connection_id"), new CharacterInventory());
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
