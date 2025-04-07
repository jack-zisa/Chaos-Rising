package dev.creoii.chaos.chat.command;

import java.util.HashMap;
import java.util.Map;

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
}
