package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.Mutable;

import javax.annotation.Nullable;
import java.util.UUID;

public class CharacterEntity extends LivingEntity {
    private final Mutable<CharacterClass> characterClass;
    private final CharacterInventory inventory;
    @Nullable
    private UUID lootUuid;

    public CharacterEntity(Game game, EntityType<? extends CharacterEntity> type, UUID uuid, Vector2 pos, Mutable<CharacterClass> characterClass, CharacterInventory inventory) {
        super(game, type, uuid, pos);
        this.characterClass = characterClass;
        this.inventory = inventory.withCharacter(this);
    }

    public Mutable<CharacterClass> getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass.set(characterClass);
        getStats().set(characterClass.baseStatContainer());
        getMaxStats().set(characterClass.baseStatContainer());
    }

    public CharacterInventory getInventory() {
        return inventory;
    }

    @Nullable
    public UUID getLootUuid() {
        return lootUuid;
    }

    public void setLootUuid(@Nullable UUID lootUuid) {
        this.lootUuid = lootUuid;
    }

    public void dropItem(ItemStack stack) {
        dropItem(stack, false);
    }

    public void dropItem(ItemStack stack, boolean forceDrop) {
        if (lootUuid == null || forceDrop) {
            LootDropEntity lootDropEntity = getGame().getEntityManager().addEntity(getGame().getDataManager().getLootDrop("bag"), getPos().cpy());
            lootDropEntity.addItem(stack);
            lootUuid = lootDropEntity.getUuid();
        } else {
            LootDropEntity lootDropEntity = (LootDropEntity) getGame().getEntityManager().getEntity(lootUuid);
            if (lootDropEntity == null || !lootDropEntity.getInventory().addItem(stack))
                dropItem(stack, true);
        }
    }
}
