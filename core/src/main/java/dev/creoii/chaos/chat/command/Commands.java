package dev.creoii.chaos.chat.command;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.effect.StatusEffects;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterClass;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Commands {
    private static final Random RANDOM = new Random();
    static final Map<String, Command> ALL = new HashMap<>();

    static {
        Command.register("set_pos", (game, args) -> {
            if (args.length > 1) {
                float x = Integer.parseInt(args[0]) * Entity.COORDINATE_SCALE;
                float y = Integer.parseInt(args[1]) * Entity.COORDINATE_SCALE;
                game.getActiveCharacter().setPos(x, y);
            }
        });

        Command.register("set_stat", (game, args) -> {
            if (args.length > 1) {
                String stat = args[0];
                int value = Integer.parseInt(args[1]);
                switch (stat) {
                    case "health" -> game.getActiveCharacter().getStats().health.set(value);
                    case "speed" -> game.getActiveCharacter().getStats().speed.set(value);
                    case "attack_speed" -> game.getActiveCharacter().getStats().attackSpeed.set(value);
                    case "defense" -> game.getActiveCharacter().getStats().defense.set(value);
                    case "attack" -> game.getActiveCharacter().getStats().attack.set(value);
                    case "vitality" -> game.getActiveCharacter().getStats().vitality.set(value);
                }
            }
        });

        Command.register("spawn", (game, args) -> {
            int argCount = args.length;

            if (argCount < 1 || argCount == 2)
                return;

            Entity enemy = game.getDataManager().getEnemy(args[0]);

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

        Command.register("give", (game, args) -> {
            if (args.length > 0) {
                int count = args.length > 1 ? Integer.valueOf(args[1]) : 1;
                for (int i = 0; i < count; ++i) {
                    Item item = game.getDataManager().getItem(args[0]);
                    if (item == null)
                        continue;
                    game.getActiveCharacter().getInventory().addItem(item.create(game).getDefaultStack().copy());
                }
            }
        });

        Command.register("set_class", (game, args) -> {
            if (args.length > 0) {
                CharacterClass characterClass = game.getDataManager().getCharacterClass(args[0]);
                if (characterClass != null)
                    game.getActiveCharacter().setCharacterClass(characterClass);
            }
        });

        Command.register("add_effect", (game, args) -> {
            int argCount = args.length;
            if (argCount < 1)
                return;

            String effectType = args[0];

            if (argCount == 1) {
                game.getActiveCharacter().addStatusEffect(StatusEffects.ALL.get(effectType), 1, 30);
            } else if (argCount == 3) {
                int amplifier = Integer.parseInt(args[1]);
                int duration = Integer.parseInt(args[2]);
                game.getActiveCharacter().addStatusEffect(StatusEffects.ALL.get(effectType), amplifier, duration);
            }
        });

        Command.register("remove_effect", (game, args) -> {
            if (args.length > 0) {
                String effectType = args[0];
                CharacterEntity character = game.getActiveCharacter();

                if ("all".equals(effectType)) {
                    character.clearStatusEffects();
                    return;
                }

                for (int i = character.getStatusEffects().size() - 1; i >= 0; --i) {
                    if (character.getStatusEffects().get(i).id().equals(effectType))
                        character.getStatusEffects().remove(i);
                }
            }
        });
    }
}
