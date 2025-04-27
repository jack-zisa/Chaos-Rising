package dev.creoii.chaos.util;

import com.badlogic.gdx.utils.JsonValue;

@FunctionalInterface
public interface Parser {
    Identifiable parse(String id, JsonValue jsonValue);
}
