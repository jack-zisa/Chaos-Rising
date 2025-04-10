package dev.creoii.chaos.chat.command;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;

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
                    case "health" -> game.getActiveCharacter().getStats().health = value;
                    case "speed" -> game.getActiveCharacter().getStats().speed = value;
                    case "attack_speed" -> game.getActiveCharacter().getStats().attackSpeed = value;
                    case "defense" -> game.getActiveCharacter().getStats().defense = value;
                    case "attack" -> game.getActiveCharacter().getStats().attack = value;
                    case "vitality" -> game.getActiveCharacter().getStats().vitality = value;
                }
            }
        });

        Command.register("spawn", (game, args) -> {
            int argCount = args.length;

            if (argCount < 3)
                return;

            Entity enemy = game.getDataManager().getEnemy(args[0]);

            if (argCount == 3) {
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
                    x1 = Math.min(x1, x2);
                    x2 = Math.max(x1 + 1, x2 + 1);
                }
                if (y1 >= y2) {
                    y1 = Math.min(y1, y2);
                    y2 = Math.max(y1 + 1, y2 + 1);
                }

                for (int i = 0; i < count; i++) {
                    float x = x1 + RANDOM.nextInt(Math.max(1, x2 - x1));
                    float y = y1 + RANDOM.nextInt(Math.max(1, y2 - y1));
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
            }
        });

        Command.register("set_item", (game, args) -> {
            if (args.length > 0) {
                game.getActiveCharacter().setCurrentItem(game.getDataManager().getItem(args[0]));
            }
        });

        Command.register("set_class", (game, args) -> {
            if (args.length > 0) {
                game.getActiveCharacter().setCharacterClass(game.getDataManager().getCharacterClass(args[0]));
            }
        });
    }
}
