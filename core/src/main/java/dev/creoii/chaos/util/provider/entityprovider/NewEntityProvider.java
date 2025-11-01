package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.EnemyEntityType;
import dev.creoii.chaos.entity.Entity;

import java.util.HashMap;

public record NewEntityProvider(String id) implements EntityProvider {
    public static final MapCodec<NewEntityProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(NewEntityProvider::id)
    ).apply(instance, NewEntityProvider::new));

    @Override
    public Type getType() {
        return Type.NEW;
    }

    @Override
    public Entity get(Context context) {
        EnemyEntityType entityType = DataManager.getEnemy(id);
        if (entityType != null)
            return entityType.create(context.game(), context.game().getEntityManager().getNextId(), context.startPos().cpy(), new HashMap<>());
        return null;
    }
}
