package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.util.LootUtils;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public class EnemyEntity extends LivingEntity {
    private final EnemyController controller;

    public EnemyEntity(Game game, EntityType<? extends EnemyEntity> type, UUID uuid, Vector2 pos) {
        super(game, type, uuid, pos, new StatContainer(), new StatContainer());
        if (!game.isClient()) {
            controller = new EnemyController(((EnemyEntityType) type).behavior());
            controller.start(this);
        } else controller = null;
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
            LootDropEntity lootDropEntity = getGame().getEntityManager().addEntity(getGame().getDataManager().getLootDrop("bag"), getPos().cpy());
            LootUtils.fillInventory(getGame(), lootDropEntity.getInventory(), ((EnemyEntityType) getType()).lootTable(), rolls);
        }
    }
}
