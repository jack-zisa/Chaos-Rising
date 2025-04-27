package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.Set;

public record ServerEntityData(Vector2 collider, Vector2 centerPos, Set<Entity> collidingWith) {
}
