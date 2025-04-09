package dev.creoii.chaos.util.provider;

import java.util.HashMap;
import java.util.Map;

public final class FloatProviders {
    static final Map<String, FloatProvider> ALL = new HashMap<>();

    public static final FloatProvider CONSTANT = FloatProvider.register("constant", (game, value, data) -> {
        return value;
    });

    public static final FloatProvider ACCELERATE = FloatProvider.register("accelerate", (game, value, data) -> {
        return value + data.getFloat("acceleration", 0f);
    });
}
