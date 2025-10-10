package dev.creoii.chaos.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.stat.StatContainer;

public record CharacterClass(String id, float scale, StatContainer baseStatContainer, StatContainer maxStatContainer) implements Identifiable {
    public static final Codec<CharacterClass> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(CharacterClass::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(CharacterClass::scale),
            StatContainer.INT_CODEC.fieldOf("base_stats").orElse(new StatContainer()).forGetter(CharacterClass::baseStatContainer),
            StatContainer.INT_CODEC.fieldOf("max_stats").orElse(new StatContainer()).forGetter(CharacterClass::maxStatContainer)
        ).apply(instance, CharacterClass::new);
    });

    public static final Codec<CharacterClass> ID_CODEC = Codec.STRING.xmap(DataManager::getCharacterClass, CharacterClass::id);
}
