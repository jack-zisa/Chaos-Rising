package dev.creoii.chaos.util.provider;

import java.util.HashMap;
import java.util.Map;

public final class FloatProviders {
    static final Map<String, FloatProvider> ALL = new HashMap<>();

    static {
        FloatProvider.register("constant", (game, value, data) -> value);
        FloatProvider.register("accelerate", (game, value, data) -> value + data.getFloat("acceleration", 0f));
        FloatProvider.register("accelerate_2", (game, value, data) -> value * data.getFloat("acceleration", 1f));
        FloatProvider.register("clamp", (game, value, data) -> {
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
}
