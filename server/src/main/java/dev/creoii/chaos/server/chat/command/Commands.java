package dev.creoii.chaos.server.chat.command;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.effect.StatusEffects;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.server.ServerGame;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.logging.Logger;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.RandomBetweenVecProvider;
import dev.creoii.chaos.util.stat.Stat;

import javax.annotation.Nullable;
import java.util.*;

public final class Commands {
    public static final Logger LOGGER = new Logger(Commands.class.getSimpleName());
    static final ObjectMap<String, Command> ALL = new ObjectMap<>();

    @Nullable
    public static Command.Result tryExecute(ServerGame game, int id, String commandType, String[] args) {
        if (Commands.ALL.containsKey(commandType)) {
            try {
                Command command = Commands.ALL.get(commandType);
                if (args.length > command.minArgs() - 1) {
                    Command.Result result = command.execute(game, id, args);
                    LOGGER.info(result.getResultMessage(commandType, args));
                    return result;
                } else {
                    LOGGER.error("Command '/ " + commandType + "' failed to execute: Not enough arguments.");
                }
            } catch (Exception e) {
                LOGGER.error(Command.Result.FAIL.getResultMessageWithReason(commandType, args, e.toString()));
            }
        } else {
            LOGGER.warn("Command '/" + commandType + "' not found");
        }
        return null;
    }

    static {
        Command.register("setpos", 2, (game, id, args) -> {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                float x = Integer.parseInt(args[0]) * Entity.COORDINATE_SCALE;
                float y = Integer.parseInt(args[1]) * Entity.COORDINATE_SCALE;
                character.setPos(x, y);
                game.getServer().sendToAllUDP(new MoveEntityS2C(character.getId(), x, y, 0f, 0f));
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("setstat", 2, (game, id, args) -> {
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
                game.getServer().sendToTCP(character.getConnectionId(), new LivingStatUpdateS2C(character.getId(), new Stat(statType, value)));
                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("spawn", 1, (game, id, args) -> {
            int argCount = args.length;

            if (argCount == 2)
                return Command.Result.FAIL;

            EnemyEntityType enemy = DataManager.getEnemy(args[0]);

            if (enemy == null)
                return Command.Result.FAIL;

            if (argCount == 1) {
                Entity entity = game.getEntityManager().getEntity(id);
                Vector2 pos = entity == null ? Vector2.Zero.cpy() : entity.getPos().cpy();
                game.getEntityManager().addEntity(enemy, pos);
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
                float x = x1 + game.getRandom().nextInt(Math.max(1, x2 - x1));
                float y = y1 + game.getRandom().nextInt(Math.max(1, y2 - y1));
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

                game.getEntityManager().addEntities(enemy, new RandomBetweenVecProvider(new ConstantVecProvider(x1, y1), new ConstantVecProvider(x2, y2)), new HashMap<>(), count);

                return Command.Result.SUCCESS;
            }
            return Command.Result.FAIL;
        });

        Command.register("give", 1, (game, id, args) -> {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
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

        Command.register("setclass", 1, (game, id, args) -> {
            CharacterClass characterClass = DataManager.getCharacterClass(args[0]);
            if (characterClass != null) {
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    character.setCharacterClass(characterClass);

                    if (!game.isClient()) {
                        game.getServer().sendToAllTCP(new EntityDisplayS2C(id, characterClass.id(), characterClass.scale()));
                        game.getServer().sendToAllTCP(new LivingStatsUpdateS2C(id, character.getStats()));
                    }

                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("say", 1, (game, _, args) -> {
            String message = args[0];
            game.getServer().sendToAllTCP(new ChatMessageReceiveS2C(new Message(message)));
            return Command.Result.SUCCESS;
        });

        Command.register("addeffect", 2, (game, id, args) -> {
            int argCount = args.length;
            if (argCount < 1)
                return Command.Result.FAIL;

            StatusEffect.Type effectType = StatusEffect.Type.valueOf(args[0].toUpperCase());

            if (argCount == 1) {
                StatusEffect type = StatusEffects.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    character.addStatusEffect(new StatusEffect.Instance(type, 1, 30));
                    return Command.Result.SUCCESS;
                }
            } else if (argCount == 3) {
                StatusEffect type = StatusEffects.ALL.get(effectType);
                if (type == null)
                    return Command.Result.FAIL;
                CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
                if (character != null) {
                    int amplifier = Integer.parseInt(args[1]);
                    int duration = Integer.parseInt(args[2]);
                    character.addStatusEffect(new StatusEffect.Instance(type, amplifier, duration));
                    return Command.Result.SUCCESS;
                }
            }
            return Command.Result.FAIL;
        });

        Command.register("removeeffect", 1, (game, id, args) -> {
            String effectType = args[0];
            CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));

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

        Command.register("damage", 1, (game, id, args) -> {
            try {
                int damage = Integer.parseInt(args[0]);
                if (damage > 0) {
                    CharacterEntity character = ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id));
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
    }
}
