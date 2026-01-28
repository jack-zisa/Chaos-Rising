package dev.creoii.chaos.server.chat.command;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.effect.StatusEffects;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.server.ServerWorld;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.logging.Logger;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.RandomBetweenVecProvider;
import dev.creoii.chaos.util.stat.Stat;
import dev.creoii.chaos.world.dungeon.Dungeon;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;
import dev.creoii.chaos.world.dungeon.room.RoomGenerator;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;
import dev.creoii.chaos.world.setpiece.Setpiece;
import dev.creoii.chaos.world.tile.Tile;

import javax.annotation.Nullable;
import java.util.*;

public final class Commands {
    public static final Logger LOGGER = new Logger(Commands.class.getSimpleName());
    static final ObjectMap<String, Command> ALL = new ObjectMap<>();

    @Nullable
    public static Command.Result tryExecute(ServerWorld world, int id, String commandType, String[] args) {
        if (Commands.ALL.containsKey(commandType)) {
            try {
                Command command = Commands.ALL.get(commandType);
                if (args.length > command.minArgs() - 1) {
                    Command.Result result = command.execute(world, id, args);
                    LOGGER.info(result.getResultMessage(commandType, args));
                    return result;
                } else {
                    LOGGER.error("Command '/ " + commandType + "' failed to execute: Not enough arguments.");
                }
            } catch (Exception e) {
                LOGGER.error(Command.Result.FAIL.getResultMessageWithReason(commandType, args, e.toString()));
                e.printStackTrace();
            }
        } else {
            LOGGER.warn("Command '/" + commandType + "' not found");
        }
        return null;
    }

    static {
        Command.register("setpos", 2, (world, id, args) -> {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                float x = Integer.parseInt(args[0]) * Entity.COORDINATE_SCALE;
                float y = Integer.parseInt(args[1]) * Entity.COORDINATE_SCALE;
                character.setPos(x, y);
                world.getGame().getServer().sendToAllUDP(new MoveEntityS2C(character.getId(), x, y, 0f, 0f));
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("setstat", 2, (world, id, args) -> {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);

            if (character != null) {
                Stat.Type statType = Stat.Type.valueOf(args[0].toUpperCase());
                int value = Integer.parseInt(args[1]);
                switch (statType) {
                    case HEALTH -> {
                        character.getStats().setHealth(value);
                        character.getMaxStats().setHealth(value);
                    }
                    case SPEED -> {
                        character.getStats().setSpeed(value);
                        character.getMaxStats().setSpeed(value);
                    }
                    case ATTACK_SPEED -> {
                        character.getStats().setAttackSpeed(value);
                        character.getMaxStats().setAttackSpeed(value);
                    }
                    case DEFENSE -> {
                        character.getStats().setDefense(value);
                        character.getMaxStats().setDefense(value);
                    }
                    case ATTACK -> {
                        character.getStats().setAttack(value);
                        character.getMaxStats().setAttack(value);
                    }
                    case VITALITY -> {
                        character.getStats().setVitality(value);
                        character.getMaxStats().setVitality(value);
                    }
                }
                world.getGame().getServer().sendToTCP(character.getConnectionId(), new LivingStatUpdateS2C(character.getId(), new Stat(statType, value)));
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("spawn", 1, (world, id, args) -> {
            int argCount = args.length;

            if (argCount == 2)
                return Command.Result.FAIL;

            EnemyEntityType enemy = DataManager.getEnemy(args[0]);

            if (enemy == null)
                return Command.Result.FAIL;

            if (argCount == 1) {
                Entity entity = world.getEntityManager().getEntity(id);
                Vector2 pos = entity == null ? Vector2.Zero.cpy() : entity.getPos().cpy();
                world.getEntityManager().addEntity(enemy, pos);
                return Command.Result.SUCCESS;
            } else if (argCount == 3) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                world.getEntityManager().addEntity(enemy, new Vector2(x, y));
                return Command.Result.SUCCESS;
            } else if (argCount == 4) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                int count = Integer.parseInt(args[3]);
                for (int i = 0; i < count; i++) {
                    world.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
                return Command.Result.SUCCESS;
            } else if (argCount == 5) {
                int x1 = Integer.parseInt(args[1]) * (int) Entity.COORDINATE_SCALE;
                int y1 = Integer.parseInt(args[2]) * (int) Entity.COORDINATE_SCALE;
                int x2 = Integer.parseInt(args[3]) * (int) Entity.COORDINATE_SCALE;
                int y2 = Integer.parseInt(args[4]) * (int) Entity.COORDINATE_SCALE;
                float x = x1 + world.getRandom().nextInt(Math.max(1, x2 - x1));
                float y = y1 + world.getRandom().nextInt(Math.max(1, y2 - y1));
                world.getEntityManager().addEntity(enemy, new Vector2(x, y));
                return Command.Result.SUCCESS;
            } else if (argCount == 6) {
                int x1 = Integer.parseInt(args[1]) * (int) Entity.COORDINATE_SCALE;
                int y1 = Integer.parseInt(args[2]) * (int) Entity.COORDINATE_SCALE;
                int x2 = Integer.parseInt(args[3]) * (int) Entity.COORDINATE_SCALE;
                int y2 = Integer.parseInt(args[4]) * (int) Entity.COORDINATE_SCALE;
                int count = Integer.parseInt(args[5]);

                if (x1 >= x2) {
                    x1 = x2;
                }
                if (y1 >= y2) {
                    y1 = y2;
                }

                world.getEntityManager().addEntities(enemy, new RandomBetweenVecProvider(new ConstantVecProvider(x1, y1), new ConstantVecProvider(x2, y2)), new HashMap<>(), count);

                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("give", 1, (world, id, args) -> {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                Item item = DataManager.getItem(args[0]);
                if (item == null)
                    return Command.Result.FAIL;

                int count = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                for (int i = 0; i < count; ++i) {
                    character.getInventory().addItem(item.getDefaultStack().copy());
                }
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("setclass", 1, (world, id, args) -> {
            CharacterClass characterClass = DataManager.getCharacterClass(args[0]);
            if (characterClass != null) {
                CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    character.setCharacterClass(characterClass);

                    world.getGame().getServer().sendToAllTCP(new EntityDisplayS2C(id, characterClass.id(), characterClass.scale()));
                    world.getGame().getServer().sendToAllTCP(new LivingStatsUpdateS2C(id, character.getStats()));

                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("say", 1, (world, _, args) -> {
            String message = args[0];
            world.getGame().getServer().sendToAllTCP(new ChatMessageReceiveS2C(new Message(message)));
            return Command.Result.SUCCESS;
        });

        Command.register("addeffect", 2, (world, id, args) -> {
            int argCount = args.length;
            if (argCount < 1)
                return Command.Result.FAIL;

            StatusEffect.Type effectType = StatusEffect.Type.valueOf(args[0].toUpperCase());

            if (argCount == 1) {
                StatusEffect type = StatusEffects.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    character.addStatusEffect(new StatusEffect.Instance(type, 1, 30));
                    return Command.Result.SUCCESS;
                }
            } else if (argCount == 3) {
                StatusEffect type = StatusEffects.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    int amplifier = Integer.parseInt(args[1]);
                    int duration = Integer.parseInt(args[2]);
                    character.addStatusEffect(new StatusEffect.Instance(type, amplifier, duration));
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("removeeffect", 1, (world, id, args) -> {
            String effectType = args[0];
            CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));

            if (character != null) {
                if ("all".equals(effectType) || "*".equals(effectType)) {
                    character.clearStatusEffects();
                    return Command.Result.FAIL;
                }

                int size = character.getStatusEffects().size();
                for (int i = size - 1; i >= 0; --i) {
                    if (character.getStatusEffects().get(i).getEffect().id().equals(effectType))
                        character.getStatusEffects().remove(i);
                }
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("damage", 1, (world, id, args) -> {
            try {
                int damage = Integer.parseInt(args[0]);
                if (damage > 0) {
                    CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                    if (character != null) {
                        character.damage(damage);
                        return Command.Result.SUCCESS;
                    }
                }
            } catch (NumberFormatException e) {
                return Command.Result.FAIL;
            }
            return Command.Result.FAIL;
        });

        Command.register("heal", 1, (world, id, args) -> {
            try {
                int health = Integer.parseInt(args[0]);
                if (health > 0) {
                    CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                    if (character != null) {
                        character.heal(health);
                        return Command.Result.SUCCESS;
                    }
                }
            } catch (NumberFormatException e) {
                return Command.Result.FAIL;
            }
            return Command.Result.FAIL;
        });

        Command.register("experience", 1, (world, id, args) -> {
            try {
                int amount = Integer.parseInt(args[0]);
                if (amount > 0) {
                    CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                    if (character != null) {
                        character.giveExperience(amount);
                        return Command.Result.SUCCESS;
                    }
                }
            } catch (NumberFormatException e) {
                return Command.Result.FAIL;
            }
            return Command.Result.FAIL;
        });

        Command.register("levelup", 0, (world, id, args) -> {
            CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
            if (character != null) {
                character.levelUp(true);
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("settile", 1, (world, id, args) -> {
            if (args.length == 1) {
                CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    int x = Math.round(character.getPos().x / Entity.COORDINATE_SCALE);
                    int y = Math.round(character.getPos().y / Entity.COORDINATE_SCALE);
                    Tile tile = DataManager.getTile(args[0]);
                    if (tile != null) {
                        world.setGround(x, y, tile);
                        return Command.Result.SUCCESS;
                    }
                }
            } else if (args.length == 3) {
                try {
                    int x = Integer.parseInt(args[0]);
                    int y = Integer.parseInt(args[1]);
                    Tile tile = DataManager.getTile(args[2]);
                    if (tile != null) {
                        world.setGround(x, y, tile);
                        return Command.Result.SUCCESS;
                    }
                } catch (NumberFormatException e) {
                    return Command.Result.FAIL;
                }
            } else if (args.length == 4) {
                try {
                    int x = Integer.parseInt(args[0]);
                    int y = Integer.parseInt(args[1]);
                    Tile tile = DataManager.getTile(args[2]);
                    if (tile != null) {
                        String layer = args[3];
                        if (layer.equals("ground")) {
                            world.setGround(x, y, tile);
                        } else if (layer.equals("object")) {
                            world.setObject(x, y, tile);
                        }
                        return Command.Result.SUCCESS;
                    }
                } catch (NumberFormatException e) {
                    return Command.Result.FAIL;
                }
            } else if (args.length == 5) {
                try {
                    int x1 = Integer.parseInt(args[0]);
                    int y1 = Integer.parseInt(args[1]);
                    int x2 = Integer.parseInt(args[2]);
                    int y2 = Integer.parseInt(args[3]);
                    Tile tile = DataManager.getTile(args[4]);
                    if (tile != null) {
                        world.setGroundArea(x1, y1, x2, y2, tile);
                        return Command.Result.SUCCESS;
                    }
                } catch (NumberFormatException e) {
                    return Command.Result.FAIL;
                }
            } else if (args.length == 6) {
                try {
                    int x1 = Integer.parseInt(args[0]);
                    int y1 = Integer.parseInt(args[1]);
                    int x2 = Integer.parseInt(args[2]);
                    int y2 = Integer.parseInt(args[3]);
                    Tile tile = DataManager.getTile(args[4]);
                    if (tile != null) {
                        String layer = args[5];
                        if (layer.equals("ground")) {
                            world.setGroundArea(x1, y1, x2, y2, tile);
                        } else if (layer.equals("object")) {
                            world.setObjectArea(x1, y1, x2, y2, tile);
                        }
                        return Command.Result.SUCCESS;
                    }
                } catch (NumberFormatException e) {
                    return Command.Result.FAIL;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("place", 2, (world, id, args) -> {
            int x = 0;
            int y = 0;

            if (args.length == 2) {
                CharacterEntity character = ((CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    x = Math.round(character.getPos().x / Entity.COORDINATE_SCALE);
                    y = Math.round(character.getPos().y / Entity.COORDINATE_SCALE);
                }
            } else if (args.length == 4) {
                try {
                    x = Integer.parseInt(args[2]);
                    y = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    return Command.Result.FAIL;
                }
            }

            switch (args[0]) {
                case "setpiece" -> {
                    Setpiece setpiece = DataManager.getSetpiece(args[1]);
                    if (setpiece != null) {
                        world.placeSetpiece(setpiece, x, y);
                        return Command.Result.SUCCESS;
                    }
                }
                case "room" -> {
                    RoomTemplate roomTemplate = DataManager.getRoomTemplate(args[1]);
                    if (roomTemplate != null) {
                        DungeonGenerator dungeonGenerator = new DungeonGenerator(new Dungeon("placeholder", ConstantNumberProvider.ONE, roomTemplate), x, y);
                        RoomGenerator roomGenerator = new RoomGenerator(roomTemplate, x, y, null);
                        roomGenerator.place(world, dungeonGenerator, roomGenerator.build(world, dungeonGenerator));
                        return Command.Result.SUCCESS;
                    }
                }
                case "dungeon" -> {
                    Dungeon dungeon = DataManager.getDungeon(args[1]);
                    if (dungeon != null) {
                        DungeonGenerator generator = new DungeonGenerator(dungeon, x, y);
                        generator.generate(world);
                        return Command.Result.SUCCESS;
                    }
                }
                default -> {
                    return Command.Result.FAIL;
                }
            }
            return Command.Result.FAIL;
        });
    }
}
