package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.floatprovider.ConstantFloatProvider;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;

public interface VecProvider extends Provider<Vector2> {
    VecProvider copy();

    static VecProvider parse(JsonValue jsonValue) {
        if (jsonValue.isArray()) {
            FloatProvider a = new ConstantFloatProvider(jsonValue.getFloat(0));
            FloatProvider b = new ConstantFloatProvider(jsonValue.getFloat(1));
            return new ConstantVecProvider(a, b);
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                BinaryVecProvider.Operation operation = BinaryVecProvider.Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryVecProvider(a, b, operation);
            }
            case "constant" -> {
                FloatProvider x = FloatProvider.parse(jsonValue.get("x"));
                FloatProvider y = FloatProvider.parse(jsonValue.get("y"));
                yield new ConstantVecProvider(x, y);
            }
            case "mouse_pos" -> new MousePosVecProvider();
            case "normalized" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new NormalizedVecProvider(value);
            }
            case "perpendicular" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new PerpendicularVecProvider(value);
            }
            case "source_pos" -> new SourceVecProvider();
            case "target_pos" -> new TargetPosVecProvider();
            default -> throw new IllegalStateException("Unexpected VecProvider value: " + type);
        };
    }
}
