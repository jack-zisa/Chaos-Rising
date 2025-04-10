package dev.creoii.chaos.util.provider;

import java.util.HashMap;
import java.util.Map;

public final class IntProviders {
    static final Map<String, IntProvider> ALL = new HashMap<>();

    static {
        IntProvider.register("constant", (game, value, data) -> value);
        IntProvider.register("accelerate", (game, value, data) -> value + data.getInt("acceleration", 0));
        IntProvider.register("accelerate_2", (game, value, data) -> value * data.getInt("acceleration", 1));
        IntProvider.register("clamp", (game, value, data) -> {
            int min;
            if (data.has("min") && value < (min = data.getInt("min"))) {
                value = min;
            }
            int max;
            if (data.has("max") && value > (max = data.getInt("max"))) {
                value = max;
            }
            return value;
        });
    }
}
