package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.BinaryOperation;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record BinaryTransition(Transition a, Transition b, BinaryOperation operation, PhaseProvider target) implements Transition {
    public static final MapCodec<BinaryTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("a").forGetter(BinaryTransition::a),
            Transition.CODEC.fieldOf("b").forGetter(BinaryTransition::b),
            BinaryOperation.CODEC.fieldOf("operation").forGetter(BinaryTransition::operation),
            PhaseProvider.CODEC.fieldOf("target").forGetter(BinaryTransition::target)
        ).apply(instance, (a, b, operation, target) -> new BinaryTransition(a, b, operation, (PhaseProvider) target.optimize()));
    });
    public static final MapCodec<BinaryTransition> AND_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("a").forGetter(BinaryTransition::a),
            Transition.CODEC.fieldOf("b").forGetter(BinaryTransition::b),
            PhaseProvider.CODEC.fieldOf("target").forGetter(BinaryTransition::target)
        ).apply(instance, (a, b, target) -> new BinaryTransition(a, b, BinaryOperation.AND, (PhaseProvider) target.optimize()));
    });
    public static final MapCodec<BinaryTransition> OR_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("a").forGetter(BinaryTransition::a),
            Transition.CODEC.fieldOf("b").forGetter(BinaryTransition::b),
            PhaseProvider.CODEC.fieldOf("target").forGetter(BinaryTransition::target)
        ).apply(instance, (a, b, target) -> new BinaryTransition(a, b, BinaryOperation.OR, (PhaseProvider) target.optimize()));
    });
    public static final MapCodec<BinaryTransition> XOR_CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("a").forGetter(BinaryTransition::a),
            Transition.CODEC.fieldOf("b").forGetter(BinaryTransition::b),
            PhaseProvider.CODEC.fieldOf("target").forGetter(BinaryTransition::target)
        ).apply(instance, (a, b, target) -> new BinaryTransition(a, b, BinaryOperation.XOR, (PhaseProvider) target.optimize()));
    });

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public boolean shouldTransition(Provider.Context context, int time) {
        return switch (operation) {
            case AND -> a.shouldTransition(context, time) && b.shouldTransition(context, time);
            case OR -> a.shouldTransition(context, time) || b.shouldTransition(context, time);
            case XOR -> a.shouldTransition(context, time) ^ b.shouldTransition(context, time);
        };
    }
}
