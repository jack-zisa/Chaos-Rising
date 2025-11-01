package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public abstract class Action {
    public static final Codec<Action> CODEC = Action.Type.CODEC.dispatch(Action::getType, type -> switch (type) {
        case ATTACK -> AttackAction.CODEC;
        case MOVE -> MoveAction.CODEC;
        case TELEPORT -> TeleportAction.CODEC;
        case MESSAGE -> MessageAction.CODEC;
        case KILL -> KillAction.CODEC;
        case WAIT -> WaitAction.CODEC;
    });

    public abstract Type getType();

    public abstract void start(EntityController<? extends EnemyEntity> controller);

    public abstract void update(EntityController<? extends EnemyEntity> controller, int time, float delta);

    public abstract void reset(EntityController<? extends EnemyEntity> controller);

    public enum Type {
        ATTACK,
        MOVE,
        TELEPORT,
        MESSAGE,
        KILL,
        WAIT;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
