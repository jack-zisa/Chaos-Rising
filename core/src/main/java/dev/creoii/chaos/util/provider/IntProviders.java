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
}
