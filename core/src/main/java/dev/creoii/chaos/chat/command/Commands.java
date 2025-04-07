package dev.creoii.chaos.chat.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Commands {
    static final Map<String, Command> ALL = new HashMap<>();

    public static final Command SET_POS = Command.register("set_pos", (game, args) -> {
        if (args.length > 1) {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            game.getCharacter().setPos(x, y);
        }
    });

    public static final Command SET_STAT = Command.register("set_stat", (game, args) -> {
        if (args.length > 1) {
            String stat = args[0];
            int value = Integer.parseInt(args[1]);
            switch (stat) {
                case "health" -> game.getCharacter().getStats().health = value;
                case "speed" -> game.getCharacter().getStats().speed = value;
                case "attack_speed" -> game.getCharacter().getStats().attackSpeed = value;
                case "defense" -> game.getCharacter().getStats().defense = value;
                case "attack" -> game.getCharacter().getStats().attack = value;
                case "vitality" -> game.getCharacter().getStats().vitality = value;
            }
        }
    });

    public static final Command SPAWN = Command.register("spawn", (game, args) -> {
        int argCount = args.length;
        Random random = new Random();

        if (argCount >= 1) {
            Entity enemy = game.getDataManager().getEnemy(args[0]);

            if (argCount == 1) {
                float x = random.nextInt(Gdx.graphics.getWidth());
                float y = random.nextInt(Gdx.graphics.getHeight());
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
            } else if (argCount == 2) {
                int count = Integer.parseInt(args[1]);
                for (int i = 0; i < count; i++) {
                    float x = random.nextInt(Gdx.graphics.getWidth());
                    float y = random.nextInt(Gdx.graphics.getHeight());
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
            } else if (argCount == 3) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
            } else if (argCount == 4) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                int count = Integer.parseInt(args[3]);
                for (int i = 0; i < count; i++) {
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
            } else if (argCount == 5) {
                int x1 = Integer.parseInt(args[1]);
                int y1 = Integer.parseInt(args[2]);
                int x2 = Integer.parseInt(args[3]);
                int y2 = Integer.parseInt(args[4]);
                float x = x1 + random.nextInt(Math.max(1, x2 - x1));
                float y = y1 + random.nextInt(Math.max(1, y2 - y1));
                game.getEntityManager().addEntity(enemy, new Vector2(x, y));
            } else if (argCount == 6) {
                int x1 = Integer.parseInt(args[1]);
                int y1 = Integer.parseInt(args[2]);
                int x2 = Integer.parseInt(args[3]);
                int y2 = Integer.parseInt(args[4]);
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
                    float x = x1 + random.nextInt(Math.max(1, x2 - x1));
                    float y = y1 + random.nextInt(Math.max(1, y2 - y1));
                    game.getEntityManager().addEntity(enemy, new Vector2(x, y));
                }
            }
        }
    });
}
