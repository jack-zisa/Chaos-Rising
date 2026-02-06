package dev.creoii.chaos.util;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Light(Type type, int rays, float distance, Color color, boolean isStatic, boolean soft) {
    public static final Light EMPTY = new Light(Type.POINT, 0, 0f, Color.CLEAR, false, false);
    public static final Codec<Light> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Type.CODEC.fieldOf("type").orElse(Type.POINT).forGetter(Light::type),
            Codec.INT.fieldOf("rays").orElse(64).forGetter(Light::rays),
            Codec.FLOAT.fieldOf("distance").orElse(10f).forGetter(Light::distance),
            Codecs.COLOR.fieldOf("color").orElse(Color.WHITE).forGetter(Light::color),
            Codec.BOOL.fieldOf("static").orElse(false).forGetter(Light::isStatic),
            Codec.BOOL.fieldOf("soft").orElse(true).forGetter(Light::soft)
        ).apply(instance, (type, rays, distance, color, isStatic, soft) -> rays <= 0 || distance <= 0f || color.a == 0f ? EMPTY : new Light(type, rays, distance, color, isStatic, soft));
    });

    public enum Type {
        POINT;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
