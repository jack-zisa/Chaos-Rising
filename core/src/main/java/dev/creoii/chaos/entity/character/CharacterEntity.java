package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.CharacterInventory;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.item.ItemStack;

import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private CharacterClass characterClass;
    private final EntityController<CharacterEntity> controller;
    private final Vector2 prevPos;
    private final CharacterInventory inventory;
    private UUID lootUuid;

    public CharacterEntity(CharacterClass characterClass) {
        super(characterClass.getTextureId(), 1f, new Vector2(1, 1), Group.CHARACTER, characterClass.getBaseStats().copy(), characterClass.getBaseStats().copy());
        this.characterClass = characterClass;
        controller = new CharacterController(this);
        prevPos = new Vector2();
        inventory = new CharacterInventory(this);
        lootUuid = null;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
        sprite = new Sprite(game.getTextureManager().getTexture("class", getTextureId()));
        sprite.setSize(getScale(), getScale());
        getStats().setHealth(characterClass.getBaseStats().health.base());
        getStats().setSpeed(characterClass.getBaseStats().speed.base());
        getStats().setAttackSpeed(characterClass.getBaseStats().attackSpeed.base());
        getStats().setDefense(characterClass.getBaseStats().defense.base());
        getStats().setAttack(characterClass.getBaseStats().attack.base());
        getStats().setVitality(characterClass.getBaseStats().vitality.base());
        getMaxStats().setHealth(characterClass.getBaseStats().health.base());
        getMaxStats().setSpeed(characterClass.getBaseStats().speed.base());
        getMaxStats().setAttackSpeed(characterClass.getBaseStats().attackSpeed.base());
        getMaxStats().setDefense(characterClass.getBaseStats().defense.base());
        getMaxStats().setAttack(characterClass.getBaseStats().attack.base());
        getMaxStats().setVitality(characterClass.getBaseStats().vitality.base());
    }

    @Override
    public String getTextureId() {
        return characterClass.getTextureId();
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

    public CharacterInventory getInventory() {
        return inventory;
    }

    public UUID getLootUuid() {
        return lootUuid;
    }

    public void clearLootUuid() {
        lootUuid = null;
    }

    public void dropItem(ItemStack stack) {
        dropItem(stack, false);
    }

    public void dropItem(ItemStack stack, boolean forceDrop) {
        if (lootUuid == null || forceDrop) {
            Inventory inventory = new Inventory(2, 4);
            inventory.addItem(stack);
            LootDropEntity lootDropEntity = new LootDropEntity("bag", 1f, true, inventory);
            game.getEntityManager().addEntity(lootDropEntity, pos.cpy());
            lootUuid = lootDropEntity.getUuid();
        } else {
            LootDropEntity lootDropEntity = (LootDropEntity) game.getEntityManager().getEntity(lootUuid);
            if (lootDropEntity == null || !lootDropEntity.getInventory().addItem(stack))
                dropItem(stack, true);
        }
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
    public void onDeath() {

    }

    @Override
    public EntityController<CharacterEntity> getController() {
        return controller;
    }

    @Override
    public void collisionEnter(Entity other) {
        if (other instanceof LootDropEntity lootDropEntity) {
            if (lootUuid == null)
                lootUuid = lootDropEntity.getUuid();
        }
    }

    @Override
    public void collisionExit(Entity other) {
        if (other == null)
            return;
        if (other.getUuid().equals(lootUuid))
            clearLootUuid();
    }
}
