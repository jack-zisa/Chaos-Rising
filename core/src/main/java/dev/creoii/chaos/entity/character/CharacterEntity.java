package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.item.Item;

import javax.annotation.Nullable;
import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private CharacterClass characterClass;
    private final EntityController<CharacterEntity> controller;
    @Nullable
    private Item currentItem;

    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.spritePath(), 1f, new Vector2(1, 1), Group.CHARACTER, characterClass.baseStats().copy(), characterClass.baseStats().copy());
        this.characterClass = characterClass;
        controller = new CharacterController(this);
        currentItem = null;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    @Nullable
    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(@Nullable Item currentItem) {
        this.currentItem = currentItem;
    }

    public Item removeCurrentItem() {
        Item temp = currentItem;
        currentItem = null;
        return temp;
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        sprite = new Sprite(game.getTextureManager().getTexture("class", getTextureId()));
        sprite.setSize(getScale(), getScale());
        return this;
    }

    @Override
    public void postSpawn() {

    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return controller;
    }

    @Override
    public void collide(Entity other) {
    }
}
