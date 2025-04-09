package dev.creoii.chaos.util.provider;

import java.util.HashMap;
import java.util.Map;

public final class IntProviders {
    static final Map<String, IntProvider> ALL = new HashMap<>();

    public static final IntProvider CONSTANT = IntProvider.register("constant", (game, value, data) -> {
        return value;
    });

    public static final IntProvider ACCELERATE = IntProvider.register("accelerate", (game, value, data) -> {
        return value + data.getInt("acceleration", 0);
    });

    public static final IntProvider ACCELERATE_2 = IntProvider.register("accelerate_2", (game, value, data) -> {
        return value * data.getInt("acceleration", 1);
    });

    public static final IntProvider CLAMP = IntProvider.register("clamp", (game, value, data) -> {
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
