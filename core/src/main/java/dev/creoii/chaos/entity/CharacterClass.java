package dev.creoii.chaos.entity;

import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.stat.StatContainer;

public record CharacterClass(String id, float scale, StatContainer baseStatContainer, StatContainer maxStatContainer) implements Identifiable {
}
