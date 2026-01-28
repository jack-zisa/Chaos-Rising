package dev.creoii.chaos.entity.controller.bulletpath;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class SimpleBulletPath implements BulletPath {
    public static final MapCodec<SimpleBulletPath> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("speed").forGetter(SimpleBulletPath::getSpeed),
            VecProvider.CODEC.fieldOf("offset").orElse(ConstantVecProvider.ZERO).forGetter(SimpleBulletPath::getOffset),
            NumberProvider.CODEC.fieldOf("arc_speed").orElse(ConstantNumberProvider.ZERO).forGetter(SimpleBulletPath::getArcSpeed)
        ).apply(instance, (speed, offset, arcSpeed) -> new SimpleBulletPath((NumberProvider) speed.optimize(), (VecProvider) offset.optimize(), (NumberProvider) arcSpeed.optimize()));
    });
    private final NumberProvider speed;
    private final VecProvider offset;
    private final NumberProvider arcSpeed;
    private Context context;

    public SimpleBulletPath(NumberProvider speed, VecProvider offset, NumberProvider arcSpeed) {
        this.speed = speed;
        this.offset = offset;
        this.arcSpeed = arcSpeed;
    }

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    public NumberProvider getSpeed() {
        return speed;
    }

    public VecProvider getOffset() {
        return offset;
    }

    public NumberProvider getArcSpeed() {
        return arcSpeed;
    }

    @Override
    public float speed(EntityController<? extends BulletEntity> controller) {
        if (context == null) {
            context = Context.rootOf(controller.getEntity());
        }
        return speed.init(controller.getEntity().getWorld().getGame().getGametime()).get(context);
    }

    @Override
    public void update(EntityController<? extends BulletEntity> controller, int gametime, float dt) {
        float speed = this.speed.init(gametime).get(context);
        if (speed == 0)
            return;

        Vector2 forward = new Vector2(controller.getEntity().getDirection()).scl(speed * dt);
        controller.getEntity().getPos().add(forward).add(offset.get(context));

        float angle = (float) (Math.atan2(controller.getEntity().getDirection().y, controller.getEntity().getDirection().x) + arcSpeed.init(gametime).get(context));
        controller.getEntity().getDirection().set((float) Math.cos(angle), (float) Math.sin(angle));
    }

    @Override
    public BulletPath copy() {
        return new SimpleBulletPath(speed.copy(), offset.copy(), arcSpeed.copy());
    }
}
