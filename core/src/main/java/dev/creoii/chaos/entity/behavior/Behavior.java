package dev.creoii.chaos.entity.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EnemyController;

public interface Behavior {
    Codec<Behavior> CODEC = Codec.PASSTHROUGH.comapFlatMap(
        dynamic -> {
            String typeString = dynamic.get("type").asString().result().orElse("empty");
            Type type = Type.valueOf(typeString.toUpperCase());

            return switch (type) {
                case SIMPLE -> SimpleBehavior.CODEC.codec().parse(dynamic);
                case MULTI -> MultiBehavior.CODEC.codec().parse(dynamic);
                case EMPTY -> EmptyBehavior.CODEC.codec().parse(dynamic);
            };
        },
        behavior -> {
            Type type = behavior.getType();
            return switch (type) {
                case SIMPLE -> new Dynamic<>(JsonOps.INSTANCE, SimpleBehavior.CODEC.codec().encodeStart(JsonOps.INSTANCE, (SimpleBehavior) behavior).getOrThrow());
                case MULTI -> new Dynamic<>(JsonOps.INSTANCE, MultiBehavior.CODEC.codec().encodeStart(JsonOps.INSTANCE, (MultiBehavior) behavior).getOrThrow());
                case EMPTY -> new Dynamic<>(JsonOps.INSTANCE, EmptyBehavior.CODEC.codec().encodeStart(JsonOps.INSTANCE, (EmptyBehavior) behavior).getOrThrow());
            };
        }
    );

    Type getType();

    void start(EnemyController controller, EnemyEntity entity);

    void update(EnemyController controller, int time, float delta);

    Behavior copy();

    enum Type {
        SIMPLE,
        MULTI,
        EMPTY;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
