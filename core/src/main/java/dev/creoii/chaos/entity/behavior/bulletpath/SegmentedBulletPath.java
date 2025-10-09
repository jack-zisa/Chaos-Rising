package dev.creoii.chaos.entity.behavior.bulletpath;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;

import java.util.*;

public record SegmentedBulletPath(TreeMap<Integer, BulletPath> segments) implements BulletPath {
    public static final MapCodec<SegmentedBulletPath> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.list(Segment.CODEC).fieldOf("segments").forGetter(segmentedBulletPath -> segmentedBulletPath.segments.entrySet().stream().map(entry -> new Segment(entry.getKey(), entry.getValue())).toList())
        ).apply(instance, segments -> {
            TreeMap<Integer, BulletPath> map = new TreeMap<>();
            for (Segment segment : segments) {
                map.put(segment.threshold(), segment.path());
            }
            return new SegmentedBulletPath(map);
        })
    );

    @Override
    public Type getType() {
        return Type.SEGMENTED;
    }

    public SegmentedBulletPath(Map<Integer, BulletPath> segments) {
        this(new TreeMap<>(Comparator.reverseOrder()));
        this.segments.putAll(segments);
    }

    @Override
    public float speed(EntityController<? extends BulletEntity> controller) {
        int lifetime = controller.getEntity().getLifetime();
        Map.Entry<Integer, BulletPath> entry = segments.floorEntry(lifetime);
        return entry != null ? entry.getValue().speed(controller) : 0f;
    }

    @Override
    public void update(EntityController<? extends BulletEntity> controller, int gametime, float dt) {
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

    public record Segment(int threshold, BulletPath path) {
        public static final Codec<Segment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("threshold").forGetter(Segment::threshold),
            BulletPath.CODEC.fieldOf("path").forGetter(Segment::path)
        ).apply(instance, Segment::new));
    }
}
