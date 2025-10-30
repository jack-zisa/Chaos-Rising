package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.Comparator;

public record NearestCharacterEntityProvider() implements EntityProvider {
    public static final NearestCharacterEntityProvider INSTANCE = new NearestCharacterEntityProvider();
    public static final MapCodec<NearestCharacterEntityProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.NEAREST_CHARACTER;
    }

    @Override
    @Nullable
    public Entity get(Context context) {
        Game game = context.game();
        EntityManager<?> entityManager = game.getEntityManager();
        return entityManager.getEntities(EntityGroup.CHARACTER).values().stream()
            .map(o -> (CharacterEntity) o)
            .min(Comparator.comparingDouble(c -> context.sourceEntity().getPos().dst2(c.getPos())))
            .orElse(null);
    }
}
