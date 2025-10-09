package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.entity.LivingEntity;

public record HasEffectBooleanProvider(String effect) implements BooleanProvider {
    @Override
    public Boolean get(Context context) {
        if (context.sourceEntity() instanceof LivingEntity livingEntity) {
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
