package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.Context;
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
        ).apply(instance, (entity, pos, makeChildren) -> new SpawnAction((EntityProvider) entity.optimize(), (VecProvider) pos.optimize(), (BooleanProvider) makeChildren.optimize()));
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
        World world = entity.getWorld();
        if (!world.getGame().isClient()) {
            Context context = Context.rootOf(entity);
            Entity spawned = this.entity.get(context);
            if (world.getEntityManager().getEntity(spawned.getType().group(), spawned.getId()) != null)
                return;

            if (spawned instanceof LivingEntity living && makeChildren.get(context)) {
                entity.addChild(spawned.getId());
                living.setParentId(entity.getId());
            }

            Vector2 pos = this.pos.get(context);
            spawned.setPos(pos.x, pos.y);
            world.getEntityManager().addEntity(spawned);
        }
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }
}
