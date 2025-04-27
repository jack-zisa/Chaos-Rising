package dev.creoii.chaos.entity.behavior.bulletpath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

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
                NumberProvider speed = NumberProvider.parse(pathValue.get("speed"), 0f).copy();
                VecProvider offset = pathValue.has("offset") ? VecProvider.parse(pathValue.get("offset")) : new ConstantVecProvider(Vector2.Zero);
                NumberProvider arcSpeed = NumberProvider.parse(pathValue.get("arc_speed"), 0f).copy();
                return new SimpleBulletPath(speed, offset, arcSpeed);
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
