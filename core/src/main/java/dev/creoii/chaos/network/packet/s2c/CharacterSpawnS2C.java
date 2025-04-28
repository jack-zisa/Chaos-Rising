package dev.creoii.chaos.network.packet.s2c;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.UUID;

public record CharacterSpawnS2C(UUID uuid, String classId, Vector2 pos) implements Serializable {
}
