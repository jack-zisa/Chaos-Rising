package dev.creoii.chaos.entity.serialization;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.EntityGroup;

public record BulletData(float xd, float yd) implements EntityCustomData {
    public static final MapCodec<BulletData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.FLOAT.fieldOf("xd").forGetter(BulletData::xd),
        Codec.FLOAT.fieldOf("yd").forGetter(BulletData::yd)
    ).apply(instance, BulletData::new));

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.BULLET;
    }

    @Override
    public MapCodec<BulletData> getCodec() {
        return CODEC;
    }

    @Override
    public void write(Output output) {
        output.writeFloat(xd);
        output.writeFloat(yd);
    }

    public static BulletData read(Input input) {
        return new BulletData(input.readFloat(), input.readFloat());
    }
}
