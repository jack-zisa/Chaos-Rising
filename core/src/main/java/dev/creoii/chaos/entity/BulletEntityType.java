package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.bulletpath.BulletPath;
import dev.creoii.chaos.entity.controller.bulletpath.EmptyBulletPath;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.booleanprovider.ConstantBooleanProvider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.Map;
import java.util.UUID;

public record BulletEntityType(String id, float scale, int lifetime, NumberProvider angleOffset, BulletPath path, BooleanProvider piercing) implements EntityType<BulletEntity> {
    public static final MapCodec<BulletEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(BulletEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(BulletEntityType::scale),
            Codec.INT.fieldOf("lifetime").orElse(1).forGetter(BulletEntityType::lifetime),
            NumberProvider.CODEC.fieldOf("angle_offset").orElse(ConstantNumberProvider.ZERO).forGetter(BulletEntityType::angleOffset),
            BulletPath.CODEC.fieldOf("path").orElse(EmptyBulletPath.INSTANCE).forGetter(BulletEntityType::path),
            BooleanProvider.CODEC.fieldOf("piercing").orElse(ConstantBooleanProvider.FALSE).forGetter(BulletEntityType::piercing)
        ).apply(instance, BulletEntityType::new);
    });

    @Override
    public EntityGroup group() {
        return EntityGroup.BULLET;
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public BulletEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        BulletEntity bullet = new BulletEntity(game, this, uuid, pos.cpy(), (Vector2) customData.get("direction"), lifetime, (int) customData.get("damage"), (int) customData.get("index"));
        /*bullet.centerPos = new Vector2();
        bullet.colliderRect = new Rectangle();
        bullet.colliderRect.setPosition(pos);
        bullet.colliderRect.setSize(scale());
        bullet.collidingWith = new HashSet<>();
        bullet.spawnTime = game.getGametime();*/
        /*bullet.getCenterPos();*/
        /*bullet.postSpawn();*/
        return bullet;
    }
}
