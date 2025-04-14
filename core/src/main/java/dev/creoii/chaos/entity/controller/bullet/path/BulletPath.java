package dev.creoii.chaos.entity.controller.bullet.path;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.util.provider.FloatProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.HashMap;
import java.util.Map;

public interface BulletPath {
    float speed(BulletController controller);

    void update(BulletController controller, int gametime, float dt);

    BulletPath copy();

    static BulletPath parse(JsonValue jsonValue) {
        if (jsonValue.has("path")) {
            JsonValue pathValue = jsonValue.get("path");
            if (pathValue.has("speed") || pathValue.has("frequency") || pathValue.has("amplitude") || pathValue.has("arc_speed")) {
                Provider<Float> speed = FloatProvider.parse(pathValue.get("speed"));
                Provider<Float> frequency = FloatProvider.parse(pathValue.get("frequency"));
                Provider<Float> amplitude = FloatProvider.parse(pathValue.get("amplitude"));
                Provider<Float> arcSpeed = FloatProvider.parse(pathValue.get("arc_speed"));
                return new SimpleBulletPath(speed, frequency, amplitude, arcSpeed);
            } else if (pathValue.has("segments")) {
                JsonValue segmentsList = pathValue.get("segments");
                Map<Integer, BulletPath> segments = new HashMap<>();
                segmentsList.forEach(jsonValue1 -> segments.put(jsonValue1.getInt("threshold"), parse(jsonValue1)));
                return new SegmentedBulletPath(segments);
            }
        }
        return new EmptyBulletPath();
    }
}
