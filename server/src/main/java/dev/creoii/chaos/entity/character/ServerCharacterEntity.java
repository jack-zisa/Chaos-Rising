package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.effect.ServerStatusEffect;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.entity.behavior.EntityController;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.s2c.LootDropCloseS2C;
import dev.creoii.chaos.network.packet.s2c.LootDropOpenS2C;
import dev.creoii.chaos.network.packet.s2c.StatusEffectS2C;
import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class ServerCharacterEntity extends CharacterEntity {
    private final int connectionId;
    private final EntityController<ServerCharacterEntity> controller;
    private final Vector2 prevPos;
    private final CharacterInventory inventory;
    private UUID lootUuid;

    public ServerCharacterEntity(int connectionId, CharacterEntityType characterEntityType) {
        super(characterEntityType, EntityGroup.CHARACTER, characterEntityType.characterClass().get().baseStatContainer().copy(), characterEntityType.characterClass().get().baseStatContainer().copy());
        this.connectionId = connectionId;
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
            ServerLootDropEntity lootDropEntity = game.getEntityManager().addEntity(game.getDataManager().getLootDrop("bag"), pos.cpy());
            Inventory inventory = new Inventory(2, 4);
            inventory.addItem(stack);
            lootDropEntity.setInventory(inventory);
            lootUuid = lootDropEntity.getUuid();
        } else {
            ServerLootDropEntity lootDropEntity = (ServerLootDropEntity) game.getEntityManager().getEntity(lootUuid);
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
    public EntityController<ServerCharacterEntity> getController() {
        return controller;
    }

    @Override
    public void collisionEnter(ServerEntity other) {
        if (other instanceof ServerLootDropEntity lootDropEntity) {
            game.getMain().getServer().sendToTCP(connectionId, new LootDropOpenS2C(lootDropEntity.getInventory()));
        }
    }

    @Override
    public void collisionExit(ServerEntity other) {
        if (other == null)
            return;

        if (other instanceof ServerLootDropEntity) {
            game.getMain().getServer().sendToTCP(connectionId, new LootDropCloseS2C());
        }
    }

    @Override
    public void addStatusEffect(ServerStatusEffect statusEffect, int amplifier, int duration) {
        super.addStatusEffect(statusEffect, amplifier, duration);
        game.getMain().getServer().sendToAllTCP(new StatusEffectS2C(uuid, statusEffect));
    }
}
