package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.serialization.EnemyData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.LootUtils;
import dev.creoii.chaos.util.provider.Provider;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnemyEntity extends LivingEntity implements Attacker {
    private final EnemyController controller;
    private long lastAttackTime;

    public EnemyEntity(Game game, EntityType<? extends EnemyEntity> type, int id, Vector2 pos) {
        super(game, type, id, pos, ((EnemyEntityType) type).stats().copy(), ((EnemyEntityType) type).stats().copy());
        if (!game.isClient()) {
            controller = new EnemyController(((EnemyEntityType) type).behavior().copy());
            controller.start(this);
        } else controller = null;

        lastAttackTime = 0L;
    }

    @Override
    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        super.reinit(id, pos, data);
        lastAttackTime = 0;
    }

    public EnemyController getController() {
        return controller;
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
        return new EnemyData(getType().id(), getStats(), getMaxStats());
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);
        controller.control(gametime, delta);
    }

    @Override
    public void remove() {
        if (!((EnemyEntityType) getType()).lootTableId().isEmpty()) {
            LootTable lootTable = DataManager.getLootTable(((EnemyEntityType) getType()).lootTableId());
            if (lootTable != null) {
                int rolls = getGame().getRandom().nextInt(4);
                if (rolls > 0) {
                    LootDropEntityType lootDropType = DataManager.getLootDrop("bag");
                    if (lootDropType != null) {
                        LootDropEntity lootDropEntity = lootDropType.create(getGame(), getGame().getEntityManager().getNextId(), getPos().cpy(), new HashMap<>());
                        LootUtils.fillInventory(getGame(), lootDropEntity.getInventory(), lootTable, rolls);
                        if (!lootDropEntity.getInventory().isEmpty()) {
                            getGame().getEntityManager().addEntity(lootDropEntity);
                        } else lootDropEntity.remove();
                    }
                }
            }
        }

        float experience = ((EnemyEntityType) getType()).experience().get(Provider.Context.of(this, getGame().getGametime()));
        if (experience > 0f) {
            getGame().getEntityManager().getEntities(EntityGroup.CHARACTER).forEach((id, o) -> {
                if (o instanceof CharacterEntity character) {
                    character.giveExperience(Math.round(experience));
                }
            });
        }

        super.remove();
    }

    @Override
    public boolean canMove() {
        return controller.getBehavior().getType() != Behavior.Type.EMPTY;
    }
}
