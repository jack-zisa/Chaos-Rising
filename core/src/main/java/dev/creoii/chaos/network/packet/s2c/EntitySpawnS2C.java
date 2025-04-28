package dev.creoii.chaos.network.packet.s2c;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.EntityGroup;

import java.io.Serializable;
import java.util.UUID;

public record EntitySpawnS2C(EntityGroup group, UUID uuid, String typeId, Vector2 pos) implements Serializable {
}
