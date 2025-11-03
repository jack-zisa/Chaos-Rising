package dev.creoii.chaos.entity.controller.bulletpath;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record SimpleBulletPath(NumberProvider speed, VecProvider offset, NumberProvider arcSpeed) implements BulletPath {
    public static final MapCodec<SimpleBulletPath> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("speed").forGetter(SimpleBulletPath::speed),
            VecProvider.CODEC.fieldOf("offset").orElse(ConstantVecProvider.ZERO).forGetter(SimpleBulletPath::offset),
            NumberProvider.CODEC.fieldOf("arc_speed").orElse(ConstantNumberProvider.ZERO).forGetter(SimpleBulletPath::arcSpeed)
        ).apply(instance, (speed, offset, arcSpeed) -> new SimpleBulletPath((NumberProvider) speed.optimize(), (VecProvider) offset.optimize(), (NumberProvider) arcSpeed.optimize()));
    });

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public float speed(EntityController<? extends BulletEntity> controller) {
        return speed.init(controller.getEntity().getGame().getGametime()).get(Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime()));
    }

    @Override
    public void update(EntityController<? extends BulletEntity> controller, int gametime, float dt) {
        Provider.Context context = Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime());
        float speed = this.speed.init(gametime).get(context);
        if (speed == 0)
            return;

        //System.out.println(speed);

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
