package dev.creoii.chaos.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.List;
import java.util.UUID;

public class ConsumableItem extends Item {
    public static final MapCodec<ConsumableItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(ConsumableItem::id),
            Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(ConsumableItem::getRarity),
            ModifierEntry.CODEC.listOf().fieldOf("stat_bonus").orElse(List.of()).forGetter(ConsumableItem::getStatBonus)
        ).apply(instance, (id, rarity, statBonus) -> new ConsumableItem(id, rarity, statBonus, List.of()));
    });
    private final List<ModifierEntry> statBonus;
    private final List<StatusEffect> statusEffects;

    public ConsumableItem(String id, Rarity rarity, List<ModifierEntry> statBonus, List<StatusEffect> statusEffects) {
        super(id, Type.CONSUMABLE, rarity);
        this.statBonus = statBonus;
        this.statusEffects = statusEffects;
    }

    public List<ModifierEntry> getStatBonus() {
        return statBonus;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    @Override
    public boolean clickInSlot(Game game, UUID characterUuid, Slot slot, ItemStack stack) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            consume(game, characterUuid, slot, stack);
            return true;
        }
        return super.clickInSlot(game, characterUuid, slot, stack);
    }

    public void consume(Game game, UUID characterUuid, Slot slot, ItemStack stack) {
        if (!game.isClient()) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, characterUuid);
            if (getStatBonus() != null) {
                StatContainer stats = character.getStats();
                getStatBonus().forEach(modifierEntry -> {
                    switch (modifierEntry.type()) {
                        case HEALTH ->
                            stats.setHealth(Math.min(character.getMaxStats().health().value(), stats.health().value() + modifierEntry.amount()));
                        case SPEED ->
                            stats.setSpeed(Math.min(character.getMaxStats().speed().value(), stats.speed().value() + modifierEntry.amount()));
                        case ATTACK_SPEED ->
                            stats.setAttackSpeed(Math.min(character.getMaxStats().attackSpeed().value(), stats.attackSpeed().value() + modifierEntry.amount()));
                        case DEFENSE ->
                            stats.setDefense(Math.min(character.getMaxStats().defense().value(), stats.defense().value() + modifierEntry.amount()));
                        case ATTACK ->
                            stats.setAttack(Math.min(character.getMaxStats().attack().value(), stats.attack().value() + modifierEntry.amount()));
                        case VITALITY ->
                            stats.setVitality(Math.min(character.getMaxStats().vitality().value(), stats.vitality().value() + modifierEntry.amount()));
                    }
                });
            }
            if (!getStatusEffects().isEmpty()) {
                statusEffects.forEach(character::addStatusEffect);
            }
        }
        slot.setStack(ItemStack.EMPTY);
    }
}
