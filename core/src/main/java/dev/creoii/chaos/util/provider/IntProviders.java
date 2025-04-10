package dev.creoii.chaos.util.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class IntProviders {
    private static final Random RANDOM = new Random();
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
        IntProvider.register("random", (game, value, data) -> {
            int min = data.getInt("min");
            int max = data.getInt("max");
            return RANDOM.nextInt(max - min + 1) + min;
        });
    }
}
