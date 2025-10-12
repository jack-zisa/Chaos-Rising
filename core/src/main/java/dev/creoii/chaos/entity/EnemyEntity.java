package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.serialization.EnemyData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.LootUtils;

import javax.annotation.Nullable;
import java.util.Map;

public class EnemyEntity extends LivingEntity implements Attacker {
    private final EnemyController controller;
    private long lastAttackTime;

    public EnemyEntity(Game game, EntityType<? extends EnemyEntity> type, int id, Vector2 pos) {
        super(game, type, id, pos, ((EnemyEntityType) type).stats().copy(), ((EnemyEntityType) type).stats().copy());
        if (!game.isClient()) {
            controller = new EnemyController(((EnemyEntityType) type).behavior());
            controller.start(this);
        } else controller = null;

        lastAttackTime = 0L;
    }

    @Override
    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        super.reinit(id, pos, data);
        lastAttackTime = 0;
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
        return new EnemyData(getStats(), getMaxStats());
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);
        controller.control(gametime, delta);
    }

    @Override
    public void remove() {
        if (((EnemyEntityType) getType()).lootTable() != null) {
            int rolls = RANDOM.nextInt(4);
            if (rolls == 0)
                return;
            LootDropEntity lootDropEntity = getGame().getEntityManager().addEntity(DataManager.getLootDrop("bag"), getPos().cpy());
            LootUtils.fillInventory(getGame(), lootDropEntity.getInventory(), ((EnemyEntityType) getType()).lootTable(), rolls);
        }
        super.remove();
    }
}
