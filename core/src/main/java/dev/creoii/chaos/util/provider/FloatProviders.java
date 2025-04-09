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

    public static final FloatProvider ACCELERATE_2 = FloatProvider.register("accelerate_2", (game, value, data) -> {
        return value * data.getFloat("acceleration", 1f);
    });

    public static final FloatProvider CLAMP = FloatProvider.register("clamp", (game, value, data) -> {
        float min;
        if (data.has("min") && value < (min = data.getFloat("min"))) {
            value = min;
        }
        float max;
        if (data.has("max") && value > (max = data.getFloat("max"))) {
            value = max;
        }
        return value;
    });
}
