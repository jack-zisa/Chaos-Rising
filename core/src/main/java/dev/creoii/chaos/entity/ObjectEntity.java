package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.serialization.EnemyData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.LootUtils;
import dev.creoii.chaos.util.context.Context;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ObjectEntity extends LivingEntity {
    public ObjectEntity(World world, EntityType<? extends ObjectEntity> type, int id, Vector2 pos) {
        super(world, type, id, pos, ((ObjectEntityType) type).stats().copy(), ((ObjectEntityType) type).stats().copy());
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new EnemyData(getType().id(), getStats(), getMaxStats());
    }

    @Override
    public void remove() {
        if (!((ObjectEntityType) getType()).lootTableId().isEmpty()) {
            LootTable lootTable = DataManager.getLootTable(((ObjectEntityType) getType()).lootTableId());
            if (lootTable != null) {
                int rolls = getWorld().getGame().getRandom().nextInt(4);
                if (rolls > 0) {
                    LootDropEntityType lootDropType = DataManager.getLootDrop("bag");
                    if (lootDropType != null) {
                        LootDropEntity lootDropEntity = lootDropType.create(getWorld(), getWorld().getEntityManager().getNextId(), getPos().cpy(), new HashMap<>());
                        LootUtils.fillInventory(getWorld(), lootDropEntity.getInventory(), lootTable, rolls);
                        if (!lootDropEntity.getInventory().isEmpty()) {
                            getWorld().getEntityManager().addEntity(lootDropEntity);
                        } else lootDropEntity.remove();
                    }
                }
            }
        }

        float experience = ((ObjectEntityType) getType()).experience().get(Context.rootOf(this));
        if (experience > 0f) {
            getWorld().getEntityManager().getEntities(EntityGroup.CHARACTER).forEach((id, o) -> {
                if (o instanceof CharacterEntity character) {
                    character.giveExperience(Math.round(experience));
                }
            });
        }

        super.remove();
    }

    @Override
    public TileCollisionType getTileCollisionType() {
        return TileCollisionType.BLOCK;
    }
}
