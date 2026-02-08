package dev.creoii.chaos.util.context;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;
import dev.creoii.chaos.world.dungeon.room.RoomGenerator;

import java.util.Random;

public final class ComponentTypes {
    public static final ComponentType<World> WORLD;
    public static final ComponentType<Entity> ENTITY;
    public static final ComponentType<Integer> TIME;
    public static final ComponentType<Vector2> POS;
    public static final ComponentType<Random> RANDOM;
    public static final ComponentType<DungeonGenerator> DUNGEON;
    public static final ComponentType<RoomGenerator> ROOM;
    public static final ComponentType<StatusEffect> STATUS_EFFECT;
    public static final ComponentType<ItemStack> ITEM_STACK;
    public static final ComponentType<Integer> ROOM_DEPTH;
    public static final ComponentType<Vector2> MOUSE_POS;
    public static final ComponentType<Vector2> TARGET_POS;

    public static void init() {}

    static {
        WORLD = ComponentType.create();
        ENTITY = ComponentType.create();
        TIME = ComponentType.create();
        POS = ComponentType.create();
        RANDOM = ComponentType.create();
        DUNGEON = ComponentType.create();
        ROOM = ComponentType.create();
        STATUS_EFFECT = ComponentType.create();
        ITEM_STACK = ComponentType.create();
        ROOM_DEPTH = ComponentType.create();
        MOUSE_POS = ComponentType.create();
        TARGET_POS = ComponentType.create();
    }
}
