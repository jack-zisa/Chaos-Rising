package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.LivingEntity;

public record HasEffectBooleanProvider(StatusEffect.Type effect) implements BooleanProvider {
    public static final MapCodec<HasEffectBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            StatusEffect.Type.CODEC.fieldOf("effect").forGetter(HasEffectBooleanProvider::effect)
        ).apply(instance, HasEffectBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.IS_CLASS;
    }

    @Override
    public Boolean get(Context context) {
        if (context.entity() instanceof LivingEntity livingEntity) {
            return livingEntity.hasStatusEffect(effect);
        }
        return false;
    }

    @Override
    public HasEffectBooleanProvider copy() {
        return new HasEffectBooleanProvider(effect);
    }

    public HasEffectBooleanProvider init(int startTime) {
        return this;
    }
}
