package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.serialization.CharacterData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.Mutable;

import javax.annotation.Nullable;

public class CharacterEntity extends LivingEntity {
    private final int connectionId;
    private final CharacterInventory inventory;
    private int lootId = -1;

    public CharacterEntity(Game game, EntityType<? extends CharacterEntity> type, int id, Vector2 pos, int connectionId, CharacterInventory inventory) {
        super(game, type, id, pos, ((CharacterEntityType) type).characterClass().get().baseStatContainer().copy(), ((CharacterEntityType) type).characterClass().get().baseStatContainer().copy());
        this.connectionId = connectionId;
        this.inventory = inventory.withCharacter(this);
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new CharacterData(getStats(), getMaxStats(), inventory.isEmpty() ? null : inventory.getSlots());
    }

    public Mutable<CharacterClass> getCharacterClass() {
        return ((CharacterEntityType) getType()).characterClass();
    }

    public void setCharacterClass(CharacterClass characterClass) {
        ((CharacterEntityType) getType()).characterClass().set(characterClass);
        getStats().set(characterClass.baseStatContainer());
        getMaxStats().set(characterClass.baseStatContainer());
    }

    public int getConnectionId() {
        return connectionId;
    }

    public CharacterInventory getInventory() {
        return inventory;
    }

    public int getLootId() {
        return lootId;
    }

    public void setLootId(int lootId) {
        this.lootId = lootId;
    }

    public void dropItem(ItemStack stack) {
        dropItem(stack, false);
    }

    public void dropItem(ItemStack stack, boolean forceDrop) {
        if (lootId == -1 || forceDrop) {
            LootDropEntity lootDropEntity = getGame().getEntityManager().addEntity(DataManager.getLootDrop("bag"), getPos().cpy());
            lootDropEntity.addItem(stack);
            lootId = lootDropEntity.getId();
        } else {
            LootDropEntity lootDropEntity = (LootDropEntity) getGame().getEntityManager().getEntity(lootId);
            if (lootDropEntity == null || lootDropEntity.getInventory().addItem(stack) == null)
                dropItem(stack, true);
        }
    }
}
