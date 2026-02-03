package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public abstract class Action {
    public static final Codec<Action> CODEC = Action.Type.CODEC.dispatch(Action::getType, type -> switch (type) {
        case ATTACK -> AttackAction.CODEC;
        case SPAWN -> SpawnAction.CODEC;
        case MOVE -> MoveAction.CODEC;
        case TELEPORT -> TeleportAction.CODEC;
        case EFFECT -> EffectAction.CODEC;
        case ORDER -> OrderAction.CODEC;
        case MESSAGE -> MessageAction.CODEC;
        case KILL -> KillAction.CODEC;
        case WAIT -> WaitAction.CODEC;
    });

    public abstract Type getType();

    public abstract void start(EntityController<? extends EnemyEntity> controller);

    public abstract void update(EntityController<? extends EnemyEntity> controller, int time, float delta);

    public abstract void end(EntityController<? extends EnemyEntity> controller);

    public abstract boolean isInstant();

    public enum Type {
        ATTACK,
        SPAWN,
        MOVE,
        TELEPORT,
        EFFECT,
        ORDER,
        MESSAGE,
        KILL,
        WAIT;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
