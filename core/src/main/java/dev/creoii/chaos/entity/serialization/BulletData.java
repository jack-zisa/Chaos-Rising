package dev.creoii.chaos.entity.serialization;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.EntityGroup;

public record BulletData(String textureId, float xd, float yd, float angleOffset) implements EntityCustomData {
    public static final MapCodec<BulletData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("texture_id").forGetter(BulletData::textureId),
        Codec.FLOAT.fieldOf("xd").forGetter(BulletData::xd),
        Codec.FLOAT.fieldOf("yd").forGetter(BulletData::yd),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(BulletData::angleOffset)
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
        output.writeString(textureId);
        output.writeFloat(xd);
        output.writeFloat(yd);
        output.writeFloat(angleOffset);
    }

    public static BulletData read(Input input) {
        return new BulletData(input.readString(), input.readFloat(), input.readFloat(), input.readFloat());
    }
}
