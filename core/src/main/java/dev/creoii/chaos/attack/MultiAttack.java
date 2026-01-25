package dev.creoii.chaos.attack;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.Attacker;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public record MultiAttack(Set<Attack> attacks) implements Attack {
    public static final MapCodec<MultiAttack> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Attack.CODEC.listOf().fieldOf("attacks").forGetter(multiAttack -> multiAttack.attacks.stream().toList())
        ).apply(instance, attacks -> {
            Set<Attack> attackSet = new HashSet<>(attacks);
            return new MultiAttack(attackSet);
        });
    });

    @Override
    public Type getType() {
        return Type.MULTI;
    }

    @Override
    public void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity, @Nullable EquipmentItem item, boolean force) {
        if (!force && !Attacker.canAttack(sourceEntity, item)) {
            return;
        }

        attacks.forEach(attack -> attack.attack(targetPos, sourcePos, sourceEntity, item, true));
    }
}
