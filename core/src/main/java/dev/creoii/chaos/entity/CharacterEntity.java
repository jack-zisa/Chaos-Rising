package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.serialization.CharacterData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.s2c.GainExperienceS2C;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;
import dev.creoii.chaos.util.event.LevelUpEvent;
import dev.creoii.chaos.world.tile.Tile;

import javax.annotation.Nullable;
import java.util.Map;

public class CharacterEntity extends LivingEntity implements Attacker {
    private int connectionId;
    private CharacterInventory inventory;
    private long lastAttackTime;
    private int lootId = -1;
    private int experience = 0;
    private int level = 0;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;

    public CharacterEntity(World world, EntityType<? extends CharacterEntity> type, int id, Vector2 pos, int connectionId) {
        super(world, type, id, pos, ((CharacterEntityType) type).characterClass().get().baseStatContainer().copy(), ((CharacterEntityType) type).characterClass().get().baseStatContainer().copy());
        this.connectionId = connectionId;
        inventory = new CharacterInventory(this);
        lastAttackTime = 0L;
    }

    @Override
    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        super.reinit(id, pos, data);
        connectionId = (int) data.get("connection_id");
        inventory = new CharacterInventory(this);
        lastAttackTime = 0L;
    }

    @Override
    public float getAttackSpeed() {
        return getStats().attackSpeed().value();
    }

    @Override
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    @Override
    public void setLastAttackTime(long attackTime) {
        lastAttackTime = attackTime;
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new CharacterData(getType().id(), getStats(), getMaxStats(), inventory.isEmpty() ? null : inventory.getSlots());
    }

    public Mutable<CharacterClass> getCharacterClass() {
        return ((CharacterEntityType) getType()).characterClass();
    }

    public void setCharacterClass(CharacterClass characterClass) {
        ((CharacterEntityType) getType()).characterClass().set(characterClass);
        getStats().set(characterClass.baseStatContainer().copy());
        getMaxStats().set(characterClass.baseStatContainer().copy());
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
            LootDropEntity lootDropEntity = getWorld().getEntityManager().addEntity(DataManager.getLootDrop("bag"), getPos().cpy());
            lootDropEntity.addItem(stack);
            lootId = lootDropEntity.getId();
        } else {
            LootDropEntity lootDropEntity = (LootDropEntity) getWorld().getEntityManager().getEntity(EntityGroup.LOOT_DROP, lootId);
            if (lootDropEntity == null || lootDropEntity.getInventory().addItem(stack) == null)
                dropItem(stack, true);
        }
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getRequiredExperienceForNextLevel() {
        return (int) Math.pow((level + 1) / .1f, 2);
    }

    public void giveExperience(int amount) {
        if (level >= 40)
            return;

        experience += amount;

        while (level < 40 && experience >= getRequiredExperienceForNextLevel()) {
            experience -= getRequiredExperienceForNextLevel();
            levelUp();
            LevelUpEvent.EVENT.invoker().onLevelUp(getWorld(), getId(), level);
        }

        if (level >= 40) {
            experience = 0;
        }

        getWorld().getGame().getServer().sendToAllTCP(new GainExperienceS2C(getId(), experience, level));
    }

    public void levelUp(boolean sync) {
        level += 1;

        if (sync) {
            getWorld().getGame().getServer().sendToAllTCP(new GainExperienceS2C(getId(), experience, level));
            LevelUpEvent.EVENT.invoker().onLevelUp(getWorld(), getId(), level);
        }
    }

    public void levelUp() {
        levelUp(false);
    }

    public boolean isMoving() {
        return moveLeft || moveRight || moveUp || moveDown;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (isMoving()) {
            float speed = getStats().speed().value();

            float vx = 0f;
            float vy = 0f;

            if (moveLeft) vx -= speed;
            if (moveRight) vx += speed;
            if (moveUp) vy += speed;
            if (moveDown) vy -= speed;

            setVelocity(vx, vy);
        } else setVelocity(0f, 0f);
    }

    public void stopMovement(boolean axis, boolean positive) {
        if (axis) {
            if (positive) moveRight = false;
            else moveLeft = false;
            getVelocity().x = 0f;
        } else {
            if (positive) moveUp = false;
            else moveDown = false;
            getVelocity().y = 0f;
        }
    }

    public void stopMovement() {
        moveRight = false;
        moveLeft = false;
        moveUp = false;
        moveDown = false;
        setVelocity(0f, 0f);
    }

    public void updateMovement(boolean axis, boolean positive) {
        if (axis) {
            if (positive) moveRight = true;
            else moveLeft = true;
        } else {
            if (positive) moveUp = true;
            else moveDown = true;
        }
    }

    @Override
    public TileCollisionType getTileCollisionType() {
        return TileCollisionType.STOP;
    }
}
