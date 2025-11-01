package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.network.s2c.EntitySpawnS2C;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.entityprovider.EntityProvider;

public class SpawnAction extends Action {
    public static final MapCodec<SpawnAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            EntityProvider.CODEC.fieldOf("entity").forGetter(SpawnAction::getEntity)
        ).apply(instance, SpawnAction::new);
    });
    private final EntityProvider entity;

    public SpawnAction(EntityProvider entity) {
        this.entity = entity;
    }

    @Override
    public Type getType() {
        return Type.SPAWN;
    }

    public EntityProvider getEntity() {
        return entity;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        Entity entity = controller.getEntity();
        Game game = entity.getGame();
        if (!game.isClient()) {
            Entity spawned = game.getEntityManager().addEntity(this.entity.get(Provider.Context.of(controller.getEntity(), game.getGametime())));
            if (game.getEntityManager().getEntity(spawned.getId()) == null)
                return;
            spawned.setPos(entity.getPos().x, entity.getPos().y);
            game.getServer().sendToAllTCP(new EntitySpawnS2C(game.getEntityManager().getNextId(), entity.getPos().x, entity.getPos().y, spawned.getType().scale(), spawned.getCustomPacketData()));
        }
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {
    }
}
