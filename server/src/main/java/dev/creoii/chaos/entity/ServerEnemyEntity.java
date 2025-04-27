package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.LootUtils;

public class ServerEnemyEntity extends ServerLivingEntity {
    private final Vector2 spawnPos;
    private final EnemyController controller;

    public ServerEnemyEntity(Vector2 spawnPos, EnemyEntityType type) {
        super(type, EntityGroup.ENEMY, type.statContainer().copy(), type.statContainer().copy());
        this.spawnPos = spawnPos;
        this.controller = new EnemyController(type.behavior());
    }

    public Vector2 getSpawnPos() {
        return spawnPos;
    }

    @Override
    public void collisionEnter(ServerEntity other) {

    }

    @Override
    public void collisionExit(ServerEntity other) {

    }

    @Override
    public void postSpawn() {
        if (controller != null)
            controller.start(this);
    }

    @Override
    public void onDeath() {
        if (((EnemyEntityType) type).lootTable() != null) {
            int rolls = RANDOM.nextInt(4);
            if (rolls == 0)
                return;
            ServerLootDropEntity lootDropEntity = game.getEntityManager().addEntity(game.getDataManager().getLootDrop("bag"), pos.cpy());
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
    public EntityController<ServerEnemyEntity> getController() {
        return controller;
    }
}
