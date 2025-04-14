package dev.creoii.chaos.entity.controller.bullet.path;

import dev.creoii.chaos.entity.controller.bullet.BulletController;

import java.util.*;

public record SegmentedBulletPath(TreeMap<Integer, BulletPath> segments) implements BulletPath {
    public SegmentedBulletPath(Map<Integer, BulletPath> segments) {
        this(new TreeMap<>(Comparator.reverseOrder()));
        this.segments.putAll(segments);
    }

    @Override
    public float speed(BulletController controller) {
        int lifetime = controller.getEntity().getLifetime();
        Map.Entry<Integer, BulletPath> entry = segments.floorEntry(lifetime);
        return entry != null ? entry.getValue().speed(controller) : 0f;
    }

    @Override
    public void update(BulletController controller, int gametime, float dt) {
        BulletPath path = segments.floorEntry(controller.getEntity().getLifetime()).getValue();

        if (path != null) {
            path.update(controller, gametime, dt);
        }
    }

    @Override
    public BulletPath copy() {
        Map<Integer, BulletPath> copied = new HashMap<>();
        for (var e : segments.entrySet()) {
            copied.put(e.getKey(), e.getValue().copy());
        }
        return new SegmentedBulletPath(copied);
    }
}
