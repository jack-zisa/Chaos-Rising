package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record RotateAngleVecProvider(VecProvider direction, NumberProvider angle) implements VecProvider {
    public static final MapCodec<RotateAngleVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("direction").forGetter(RotateAngleVecProvider::direction),
            NumberProvider.CODEC.fieldOf("angle").forGetter(RotateAngleVecProvider::angle)
        ).apply(instance, RotateAngleVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.ROTATE_ANGLE;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 direction = this.direction.get(context);

        if (direction.len() != 1f)
            direction.nor();

        return direction.rotateRad(angle.get(context));
    }

    @Override
    public VecProvider copy() {
        return new RotateAngleVecProvider(direction.copy(), angle.copy());
    }
}
