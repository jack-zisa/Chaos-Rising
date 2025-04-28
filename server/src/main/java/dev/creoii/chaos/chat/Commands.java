package dev.creoii.chaos.chat;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.effect.StatusEffectType;
import dev.creoii.chaos.effect.StatusEffectTypes;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.ServerItem;

import java.util.*;

public final class Commands {
    private static final Random RANDOM = new Random();
    static final Map<String, Command> ALL = new HashMap<>();

    public static void tryExecute(ServerGame game, UUID uuid, String commandType, String[] args) {
        if (Commands.ALL.containsKey(commandType)) {
            Commands.ALL.get(commandType).execute(game, uuid, args);
            System.out.println("[Commands] Executed '/" + commandType + "' with args " + Arrays.toString(args));
        } else {
            System.out.println("[Commands] Command '/" + commandType + "' not found");
        }
    }

    static {
        Command.register("set_pos", (game, uuid, args) -> {
            if (args.length > 1) {
                float x = Integer.parseInt(args[0]) * Entity.COORDINATE_SCALE;
                float y = Integer.parseInt(args[1]) * Entity.COORDINATE_SCALE;
                game.getEntityManager().getCharacter(uuid).setPos(x, y);
            }
        });

        Command.register("set_stat", (game, uuid, args) -> {
            if (args.length > 1) {
                String stat = args[0];
                int value = Integer.parseInt(args[1]);
                CharacterEntity character = game.getEntityManager().getCharacter(uuid);
                switch (stat) {
                    case "health" -> {
                        character.getStats().health.set(value);
                        character.getMaxStats().health.set(value);
                    }
                    case "speed" -> {
                        character.getStats().speed.set(value);
                        character.getMaxStats().speed.set(value);
                    }
                    case "attack_speed" -> {
                        character.getStats().attackSpeed.set(value);
                        character.getMaxStats().attackSpeed.set(value);
                    }
                    case "defense" -> {
                        character.getStats().defense.set(value);
                        character.getMaxStats().defense.set(value);
                    }
                    case "attack" -> {
                        character.getStats().attack.set(value);
                        character.getMaxStats().attack.set(value);
                    }
                    case "vitality" -> {
                        character.getStats().vitality.set(value);
                        character.getMaxStats().vitality.set(value);
                    }
                }
            }
        });

        Command.register("spawn", (game, uuid, args) -> {
            int argCount = args.length;

            if (argCount < 1 || argCount == 2)
                return;

            EnemyEntityType enemy = game.getDataManager().getEnemy(args[0]);

            if (enemy == null)
                return;

            if (argCount == 1) {
                game.getEntityManager().addEntity(enemy, new Vector2(0, 0));
            } else if (argCount == 3) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
            } else if (argCount == 4) {
                float x = Float.parseFloat(args[1]) * Entity.COORDINATE_SCALE;
                float y = Float.parseFloat(args[2]) * Entity.COORDINATE_SCALE;
                int count = Integer.parseInt(args[3]);
                for (int i = 0; i < count; i++) {
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
            } else if (argCount == 5) {
                int x1 = Integer.parseInt(args[1]) * (int) Entity.COORDINATE_SCALE;
                int y1 = Integer.parseInt(args[2]) * (int) Entity.COORDINATE_SCALE;
                int x2 = Integer.parseInt(args[3]) * (int) Entity.COORDINATE_SCALE;
                int y2 = Integer.parseInt(args[4]) * (int) Entity.COORDINATE_SCALE;
                float x = x1 + RANDOM.nextInt(Math.max(1, x2 - x1));
                float y = y1 + RANDOM.nextInt(Math.max(1, y2 - y1));
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
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
            }
        });

        Command.register("give", (game, uuid, args) -> {
            if (args.length > 0) {
                int count = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                for (int i = 0; i < count; ++i) {
                    ServerItem item = game.getDataManager().getItem(args[0]);
                    if (item == null)
                        continue;
                    game.getEntityManager().getCharacter(uuid).getInventory().addItem(item.getDefaultStack().copy());
                }
            }
        });

        Command.register("set_class", (game, uuid, args) -> {
            if (args.length > 0) {
                CharacterClass characterClass = game.getDataManager().getCharacterClass(args[0]);
                if (characterClass != null)
                    game.getEntityManager().getCharacter(uuid).setCharacterClass(characterClass);
            }
        });

        Command.register("add_effect", (game, uuid, args) -> {
            int argCount = args.length;
            if (argCount < 1)
                return;

            String effectType = args[0];

            if (argCount == 1) {
                StatusEffectType type = StatusEffectTypes.ALL.get(effectType);
                if (type == null)
                    return;
                game.getEntityManager().getCharacter(uuid).addStatusEffect(new StatusEffect(type, 1, 30));
            } else if (argCount == 3) {
                StatusEffectType type = StatusEffectTypes.ALL.get(effectType);
                if (type == null)
                    return;
                int amplifier = Integer.parseInt(args[1]);
                int duration = Integer.parseInt(args[2]);
                game.getEntityManager().getCharacter(uuid).addStatusEffect(new StatusEffect(type, amplifier, duration));
            }
        });

        Command.register("remove_effect", (game, uuid, args) -> {
            if (args.length > 0) {
                String effectType = args[0];
                CharacterEntity character = game.getEntityManager().getCharacter(uuid);

                if ("all".equals(effectType)) {
                    character.clearStatusEffects();
                    return;
                }

                for (int i = character.getStatusEffects().size() - 1; i >= 0; --i) {
                    if (character.getStatusEffects().get(i).getType().id().equals(effectType))
                        character.getStatusEffects().remove(i);
                }
            }
        });
    }
}
