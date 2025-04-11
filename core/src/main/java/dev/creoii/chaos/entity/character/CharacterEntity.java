package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private CharacterClass characterClass;
    private final EntityController<CharacterEntity> controller;
    private final Vector2 prevPos;
    @Nullable
    private Item currentItem;

    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.getSpritePath(), 1f, new Vector2(1, 1), Group.CHARACTER, characterClass.getBaseStats().copy(), characterClass.getBaseStats().copy());
        this.characterClass = characterClass;
        controller = new CharacterController(this);
        currentItem = null;
        prevPos = new Vector2();
    }

    @Override
    public StatContainer getStats() {
        return super.getStats();
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

    public void equipItem(@Nullable Item item) {
        unequipItem();
        if (item != null) {
            getStats().applyModifier(item.getStatModifier());
            this.currentItem = item;
        }
    }

    public void unequipItem() {
        if (currentItem != null) {
            getStats().removeModifier(currentItem.getStatModifier());
            this.currentItem = null;
        }
    }

    public Vector2 getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(Vector2 prevPos) {
        this.prevPos.set(prevPos);
    }

    public Vector2 getPrevCenterPos() {
        return new Vector2(getPrevPos()).add(COORDINATE_SCALE / 2f, COORDINATE_SCALE / 2f);
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
