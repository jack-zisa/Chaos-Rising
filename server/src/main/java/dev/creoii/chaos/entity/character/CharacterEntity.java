package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.CharacterInventory;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.util.EntityGroup;

import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private final EntityController<CharacterEntity> controller;
    private final Vector2 prevPos;
    private final CharacterInventory inventory;
    private UUID lootUuid;

    public CharacterEntity(CharacterEntityType characterEntityType) {
        super(characterEntityType, EntityGroup.CHARACTER, characterEntityType.characterClass().get().baseStatContainer().copy(), characterEntityType.characterClass().get().baseStatContainer().copy());
        controller = new CharacterController(this);
        prevPos = new Vector2();
        inventory = new CharacterInventory(this);
        lootUuid = null;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        ((CharacterEntityType) type).characterClass().set(characterClass);
        getStats().set(characterClass.baseStatContainer());
        getMaxStats().set(characterClass.baseStatContainer());
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
            LootDropEntity lootDropEntity = game.getEntityManager().addEntity(game.getDataManager().getLootDrop("bag"), pos.cpy());
            Inventory inventory = new Inventory(2, 4);
            inventory.addItem(stack);
            lootDropEntity.setInventory(inventory);
            lootUuid = lootDropEntity.getUuid();
        } else {
            LootDropEntity lootDropEntity = (LootDropEntity) game.getEntityManager().getEntity(lootUuid);
            if (lootDropEntity == null || !lootDropEntity.getInventory().addItem(stack))
                dropItem(stack, true);
        }
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
            if (lootUuid == null) {
                lootUuid = lootDropEntity.getUuid();
            }
        }
    }

    @Override
    public void collisionExit(Entity other) {
        if (other == null)
            return;

        if (other.getUuid().equals(lootUuid)) {
            clearLootUuid();
        }
    }
}
