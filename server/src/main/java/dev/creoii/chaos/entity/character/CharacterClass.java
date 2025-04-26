package dev.creoii.chaos.entity.character;

import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.stat.StatContainer;

public record CharacterClass(String id, String textureId, float scale, StatContainer baseStatContainer, StatContainer maxStatContainer) implements Identifiable {
}
