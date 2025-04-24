package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.util.LootUtils;

public class EnemyEntity extends LivingEntity {
    private final EnemyController controller;

    public EnemyEntity(EnemyEntityType type) {
        super(type, new Vector2(1, 1), Group.ENEMY, type.statContainer().copy(), type.statContainer().copy());
        this.controller = new EnemyController(type.behavior());
    }

    @Override
    public void collisionEnter(Entity other) {

    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {
        if (controller != null)
            controller.start(this);
    }

    @Override
    public void onDeath() {
        if (((EnemyEntityType) type).lootTable() != null) {
            int rolls = Entity.RANDOM.nextInt(4);
            if (rolls == 0)
                return;
            LootDropEntity lootDropEntity = game.getEntityManager().addEntity(game.getDataManager().getLootDrop("bag"), pos.cpy());
            lootDropEntity.setInventory(new Inventory(2, 4));
            LootUtils.fillInventory(getGame(), lootDropEntity.getInventory(), ((EnemyEntityType) type).lootTable(), rolls);
        }
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (getStats().health.value() <= 0)
            remove();
    }

    @Override
    public EntityController<EnemyEntity> getController() {
        return controller;
    }
}
