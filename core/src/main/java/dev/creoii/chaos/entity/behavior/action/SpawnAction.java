package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.network.s2c.EntitySpawnS2C;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.booleanprovider.ConstantBooleanProvider;
import dev.creoii.chaos.util.provider.entityprovider.EntityProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourceVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class SpawnAction extends Action {
    public static final MapCodec<SpawnAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            EntityProvider.CODEC.fieldOf("entity").forGetter(SpawnAction::getEntity),
            VecProvider.CODEC.fieldOf("pos").orElse(SourceVecProvider.INSTANCE).forGetter(SpawnAction::getPos),
            BooleanProvider.CODEC.fieldOf("make_children").orElse(ConstantBooleanProvider.TRUE).forGetter(SpawnAction::shouldMakeChildren)
        ).apply(instance, SpawnAction::new);
    });
    private final EntityProvider entity;
    private final VecProvider pos;
    private final BooleanProvider makeChildren;

    public SpawnAction(EntityProvider entity, VecProvider pos, BooleanProvider makeChildren) {
        this.entity = entity;
        this.pos = pos;
        this.makeChildren = makeChildren;
    }

    @Override
    public Type getType() {
        return Type.SPAWN;
    }

    public EntityProvider getEntity() {
        return entity;
    }

    public VecProvider getPos() {
        return pos;
    }

    public BooleanProvider shouldMakeChildren() {
        return makeChildren;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        LivingEntity entity = controller.getEntity();
        Game game = entity.getGame();
        if (!game.isClient()) {
            Provider.Context context = Provider.Context.of(entity, game.getGametime());
            Entity spawned = this.entity.get(context);
            if (game.getEntityManager().getEntity(spawned.getType().group(), spawned.getId()) != null)
                return;

            if (spawned instanceof LivingEntity living && makeChildren.get(context)) {
                entity.addChild(spawned.getId());
                living.setParentId(entity.getId());
            }

            Vector2 pos = this.pos.get(context);
            spawned.setPos(pos.x, pos.y);
            game.getEntityManager().addEntity(spawned);
            game.getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, spawned.getType().scale(), spawned.getCustomPacketData()));
        }
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }
}
