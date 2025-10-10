package dev.creoii.chaos.server.chat;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.server.ServerGame;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.effect.StatusEffectType;
import dev.creoii.chaos.effect.StatusEffectTypes;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.network.s2c.LivingStatUpdateS2C;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.logging.Logger;
import dev.creoii.chaos.util.stat.Stat;

import java.util.*;

public final class Commands {
    private static final Random RANDOM = new Random();
    public static final Logger LOGGER = new Logger(Commands.class.getSimpleName());
    static final Map<String, Command> ALL = new HashMap<>();

    public static void tryExecute(ServerGame game, int id, String commandType, String[] args) {
        if (Commands.ALL.containsKey(commandType)) {
            try {
                Command.Result result = Commands.ALL.get(commandType).execute(game, id, args);
                LOGGER.info(result.getResultMessage(commandType, args));
            } catch (Exception e) {
                LOGGER.error(Command.Result.FAIL.getResultMessageWithReason(commandType, args, e.toString()));
            }
        } else {
            LOGGER.warn("Command '/" + commandType + "' not found");
        }
    }

    static {
        Command.register("setpos", (game, id, args) -> {
            if (args.length > 1) {
                CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
                if (character != null) {
                    float x = Integer.parseInt(args[0]) * Entity.COORDINATE_SCALE;
                    float y = Integer.parseInt(args[1]) * Entity.COORDINATE_SCALE;
                    character.setPos(x, y);
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("setstat", (game, id, args) -> {
            if (args.length > 1) {
                CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);

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
                    game.getServer().sendToTCP(character.getConnectionId(), new LivingStatUpdateS2C(statType, value));
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("spawn", (game, _, args) -> {
            int argCount = args.length;

            if (argCount < 1 || argCount == 2)
                return Command.Result.FAIL;

            EnemyEntityType enemy = DataManager.getEnemy(args[0]);

            if (enemy == null)
                return Command.Result.FAIL;

            if (argCount == 1) {
                game.getEntityManager().addEntity(enemy, new Vector2(0, 0));
                return Command.Result.SUCCESS;
            } else if (argCount == 3) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                return Command.Result.SUCCESS;
            } else if (argCount == 4) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                int count = Integer.parseInt(args[3]);
                for (int i = 0; i < count; i++) {
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
                return Command.Result.SUCCESS;
            } else if (argCount == 5) {
                int x1 = Integer.parseInt(args[1]) * (int) Entity.COORDINATE_SCALE;
                int y1 = Integer.parseInt(args[2]) * (int) Entity.COORDINATE_SCALE;
                int x2 = Integer.parseInt(args[3]) * (int) Entity.COORDINATE_SCALE;
                int y2 = Integer.parseInt(args[4]) * (int) Entity.COORDINATE_SCALE;
                float x = x1 + RANDOM.nextInt(Math.max(1, x2 - x1));
                float y = y1 + RANDOM.nextInt(Math.max(1, y2 - y1));
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
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

                for (int i = 0; i < count; i++) {
                    float x = x1 + RANDOM.nextInt(Math.max(1, x2 - x1));
                    float y = y1 + RANDOM.nextInt(Math.max(1, y2 - y1));
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("give", (game, id, args) -> {
            if (args.length > 0) {
                CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
                if (character != null) {
                    int count = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                    for (int i = 0; i < count; ++i) {
                        Item item = DataManager.getItem(args[0]);
                        if (item == null)
                            continue;
                        character.getInventory().addItem(item.getDefaultStack().copy());
                    }
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("setclass", (game, id, args) -> {
            if (args.length > 0) {
                CharacterClass characterClass = DataManager.getCharacterClass(args[0]);
                if (characterClass != null) {
                    CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                    if (character != null) {
                        character.setCharacterClass(characterClass);
                        return Command.Result.SUCCESS;
                    }
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("addeffect", (game, id, args) -> {
            int argCount = args.length;
            if (argCount < 1)
                return Command.Result.FAIL;

            String effectType = args[0];

            if (argCount == 1) {
                StatusEffectType type = StatusEffectTypes.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    character.addStatusEffect(new StatusEffect(type, 1, 30));
                    return Command.Result.SUCCESS;
                }
            } else if (argCount == 3) {
                StatusEffectType type = StatusEffectTypes.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    int amplifier = Integer.parseInt(args[1]);
                    int duration = Integer.parseInt(args[2]);
                    character.addStatusEffect(new StatusEffect(type, amplifier, duration));
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("removeeffect", (game, id, args) -> {
            if (args.length > 0) {
                String effectType = args[0];
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));

                if (character != null) {
                    if ("all".equals(effectType) || "*".equals(effectType)) {
                        character.clearStatusEffects();
                        return Command.Result.FAIL;
                    }

                    for (int i = character.getStatusEffects().size() - 1; i >= 0; --i) {
                        if (character.getStatusEffects().get(i).getType().id().equals(effectType))
                            character.getStatusEffects().remove(i);
                    }
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });
    }
}
