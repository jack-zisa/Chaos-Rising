package dev.creoii.chaos.util.provider.booleanprovider;

import com.badlogic.gdx.utils.JsonValue;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.BinaryOperation;
import dev.creoii.chaos.util.provider.Comparison;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

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
            case "between" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                NumberProvider min = NumberProvider.parse(jsonValue.get("min"));
                NumberProvider max = NumberProvider.parse(jsonValue.get("max"));
                yield new BetweenBooleanProvider(value, min, max);
            }
            case "binary" -> {
                BooleanProvider a = BooleanProvider.parse(jsonValue.get("a"));
                BooleanProvider b = BooleanProvider.parse(jsonValue.get("b"));
                BinaryOperation operation = BinaryOperation.valueOf(jsonValue.getString("operation", "AND").toUpperCase());
                yield new BinaryBooleanProvider(a, b, operation);
            }
            case "and" -> {
                BooleanProvider a = BooleanProvider.parse(jsonValue.get("a"));
                BooleanProvider b = BooleanProvider.parse(jsonValue.get("b"));
                yield new BinaryBooleanProvider(a, b, BinaryOperation.AND);
            }
            case "or" -> {
                BooleanProvider a = BooleanProvider.parse(jsonValue.get("a"));
                BooleanProvider b = BooleanProvider.parse(jsonValue.get("b"));
                yield new BinaryBooleanProvider(a, b, BinaryOperation.OR);
            }
            case "xor" -> {
                BooleanProvider a = BooleanProvider.parse(jsonValue.get("a"));
                BooleanProvider b = BooleanProvider.parse(jsonValue.get("b"));
                yield new BinaryBooleanProvider(a, b, BinaryOperation.XOR);
            }
            case "constant" -> {
                boolean value = jsonValue.getBoolean("value");
                yield new ConstantBooleanProvider(value);
            }
            case "true" -> new ConstantBooleanProvider(true);
            case "false" -> new ConstantBooleanProvider(false);
            case "has_effect" -> {
                String effectId = jsonValue.getString("effect");
                yield new HasEffectBooleanProvider(effectId);
            }
            case "is_class" -> {
                String classId = jsonValue.getString("class");
                yield new IsClassBooleanProvider(classId);
            }
            case "not", "invert" -> {
                BooleanProvider value = BooleanProvider.parse(jsonValue.get("value"));
                yield new NotBooleanProvider(value);
            }
            case "number_comparison" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                Comparison comparison = Comparison.valueOf(jsonValue.getString("comparison", "E").toUpperCase());
                yield new NumberComparisonBooleanProvider(a, b, comparison);
            }
            case "rand", "random" -> new RandomBooleanProvider();
            default -> throw new IllegalStateException("Unexpected BooleanProvider value: " + type);
        };
    }

    enum Type {
        ;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
