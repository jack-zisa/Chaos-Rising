package dev.creoii.chaos.network.packet.s2c;

import dev.creoii.chaos.entity.CharacterEntity;

import java.io.Serializable;

public record CharacterSpawnS2C(CharacterEntity character) implements Serializable {
}
