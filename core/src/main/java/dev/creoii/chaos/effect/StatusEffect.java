package dev.creoii.chaos.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.Identifiable;

import java.util.function.BiConsumer;

public record StatusEffect(Type type, BiConsumer<LivingEntity, Instance> starter, BiConsumer<LivingEntity, Instance> applier, BiConsumer<LivingEntity, Instance> remover) implements Identifiable {
    public static final Codec<StatusEffect> CODEC = Type.CODEC.xmap(StatusEffects.ALL::get, StatusEffect::type);

    @Override
    public String id() {
        return type.name().toLowerCase();
    }

    public enum Type {
        REGENERATION,
        POISON,
        INVULNERABLE;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }

    public static class Instance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StatusEffect.CODEC.fieldOf("id").forGetter(Instance::getEffect),
            Codec.INT.fieldOf("amplifier").orElse(1).forGetter(Instance::getAmplifier),
            Codec.INT.fieldOf("duration").orElse(-1).forGetter(Instance::getDuration)
        ).apply(instance, Instance::new));
        private final StatusEffect effect;
        private final int amplifier;
        private int duration;
        // add frequency

        public Instance(StatusEffect effect, int amplifier, int duration) {
            this.effect = effect;
            this.amplifier = amplifier;
            this.duration = duration;
        }

        public StatusEffect getEffect() {
            return effect;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public boolean isInfinite() {
            return duration < 0;
        }

        public int getDuration() {
            return duration;
        }

        public void decrementDuration() {
            --duration;
        }
    }
}
