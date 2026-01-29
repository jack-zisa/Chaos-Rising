package dev.creoii.chaos.world.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.world.dungeon.Dungeon;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;
import dev.creoii.chaos.world.dungeon.room.Connection;
import dev.creoii.chaos.world.dungeon.room.RoomGenerator;
import it.unimi.dsi.fastutil.Pair;

public class DungeonMapGenerator implements MapGenerator {
    public static final MapCodec<DungeonMapGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(DungeonMapGenerator::id),
        Dungeon.ID_CODEC.fieldOf("dungeon").forGetter(DungeonMapGenerator::getDungeon)
    ).apply(instance, DungeonMapGenerator::new));
    private final String id;
    private final Dungeon dungeon;
    private DungeonGenerator generator;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public DungeonMapGenerator(String id, Dungeon dungeon) {
        this.id = id;
        this.dungeon = dungeon;
    }

    @Override
    public String id() {
        return id;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    @Override
    public Type getType() {
        return Type.DUNGEON;
    }

    @Override
    public int getWidth() {
        return maxX - minX;
    }

    @Override
    public int getHeight() {
        return maxY - minY;
    }

    @Override
    public void build(World world) {
        generator = new DungeonGenerator(world, dungeon, 0, 0);
        generator.build();

        for (Pair<RoomGenerator, DungeonGenerator.PendingRoom> pair : generator.getPendingRooms()) {
            DungeonGenerator.PendingRoom room = pair.right();
            minX = Math.min(minX, room.x());
            minY = Math.min(minY, room.y());
            maxX = Math.max(maxX, room.x() + room.width());
            maxY = Math.max(maxY, room.y() + room.height());
        }

        int width = maxX - minX;
        int height = maxY - minY;
        int offsetX = -minX;
        int offsetY = -minY;

        minX = 0;
        maxX = width;
        minY = 0;
        maxY = height;

        for (Pair<RoomGenerator, DungeonGenerator.PendingRoom> pair : generator.getPendingRooms()) {
            DungeonGenerator.PendingRoom room = pair.right();
            room.setPos(room.x() + offsetX, room.y() + offsetY);
            for (Connection connection : pair.left().getConnections()) {
                connection.setPos(connection.x() + offsetX, connection.y() + offsetY);
            }
        }
    }

    public void place(World world) {
        generator.place();
    }
}
