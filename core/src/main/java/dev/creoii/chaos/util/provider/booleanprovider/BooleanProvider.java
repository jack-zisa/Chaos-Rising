package dev.creoii.chaos.util.provider.booleanprovider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Provider;

public interface BooleanProvider extends Provider<Boolean> {
    BooleanProvider copy();

    BooleanProvider init(int startTime);

    static BooleanProvider parse(JsonValue jsonValue, boolean defaultValue) {
        if (jsonValue == null) {
            return new ConstantBooleanProvider(defaultValue);
        }
        return jsonValue.isNumber() || jsonValue.isObject() ? parse(jsonValue) : new ConstantBooleanProvider(defaultValue);
    }

    static BooleanProvider parse(JsonValue jsonValue) {
        if (jsonValue.isBoolean()) {
            return new ConstantBooleanProvider(jsonValue.asBoolean());
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "constant" -> {
                boolean value = jsonValue.getBoolean("value");
                yield new ConstantBooleanProvider(value);
            }
            default -> throw new IllegalStateException("Unexpected FloatProvider value: " + type);
        };
    }
}
