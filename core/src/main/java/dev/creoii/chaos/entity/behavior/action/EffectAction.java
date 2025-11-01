package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class EffectAction extends Action {
    public static final MapCodec<EffectAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatusEffect.Instance.CODEC.fieldOf("effect").forGetter(EffectAction::getEffect),
        Codec.BOOL.fieldOf("remove").forGetter(EffectAction::shouldRemove)
    ).apply(instance, EffectAction::new));
    private final StatusEffect.Instance effect;
    private final boolean remove;

    public EffectAction(StatusEffect.Instance effect, boolean remove) {
        this.effect = effect;
        this.remove = remove;
    }

    public StatusEffect.Instance getEffect() {
        return effect;
    }

    public boolean shouldRemove() {
        return remove;
    }

    @Override
    public Type getType() {
        return Type.EFFECT;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        controller.getEntity().addStatusEffect(effect);
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {

    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
        if (remove)
            controller.getEntity().removeStatusEffect(effect);
    }
}
