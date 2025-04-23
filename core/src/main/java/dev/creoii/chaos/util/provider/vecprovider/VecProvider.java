package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Operation;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.TrigFunction;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public interface VecProvider extends Provider<Vector2> {
    VecProvider copy();

    static VecProvider parse(JsonValue jsonValue) {
        if (jsonValue.isArray()) {
            NumberProvider a = new ConstantNumberProvider(jsonValue.getFloat(0));
            NumberProvider b = new ConstantNumberProvider(jsonValue.getFloat(1));
            return new ConstantVecProvider(a, b);
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                Operation operation = Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryVecProvider(a, b, operation);
            }
            case "constant" -> {
                NumberProvider x = NumberProvider.parse(jsonValue.get("x"));
                NumberProvider y = NumberProvider.parse(jsonValue.get("y"));
                yield new ConstantVecProvider(x, y);
            }
            case "direction" -> {
                VecProvider from = VecProvider.parse(jsonValue.get("from"));
                VecProvider to = VecProvider.parse(jsonValue.get("to"));
                yield new DirectionVecProvider(from, to);
            }
            case "mouse_pos" -> new MousePosVecProvider();
            case "normalized" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new NormalizedVecProvider(value);
            }
            case "parent_pos" -> new ParentVecProvider();
            case "perpendicular" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new PerpendicularVecProvider(value);
            }
            case "relative_to" -> {
                VecProvider parent = VecProvider.parse(jsonValue.get("parent"));
                VecProvider offset = VecProvider.parse(jsonValue.get("offset"));
                yield new RelativeToVecProvider(parent, offset);
            }
            case "rotate_angle" -> {
                VecProvider direction = VecProvider.parse(jsonValue.get("direction"));
                NumberProvider angle = NumberProvider.parse(jsonValue.get("angle"));
                yield new RotateAngleVecProvider(direction, angle);
            }
            case "rotated_offset" -> {
                VecProvider from = VecProvider.parse(jsonValue.get("from"));
                VecProvider to = VecProvider.parse(jsonValue.get("to"));
                VecProvider offset = VecProvider.parse(jsonValue.get("offset"));
                yield new RotatedOffsetVecProvider(from, to, offset);
            }
            case "source_pos" -> new SourceVecProvider();
            case "target_pos" -> new TargetPosVecProvider();
            case "trig" -> {
                TrigFunction function = TrigFunction.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new TrigVecProvider(function, value);
            }
            default -> throw new IllegalStateException("Unexpected VecProvider value: " + type);
        };
    }
}
