package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.JsonValue;

public class Stats {
    public int health;
    public int speed;
    public int attackSpeed;
    public int defense;
    public int attack;
    public int vitality;

    public Stats() {
        this(1, 1, 1, 0, 1, 1);
    }

    public Stats(int health, int speed, int attackSpeed, int defense, int attack, int vitality) {
        this.health = health;
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.defense = defense;
        this.attack = attack;
        this.vitality = vitality;
    }

    public Stats copy() {
        return new Stats(health, speed, attackSpeed, defense, attack, vitality);
    }

    public static Stats fromJson(JsonValue json) {
        return new Stats(
            json.getInt("health", 1),
            json.getInt("speed", 1),
            json.getInt("attack_speed", 1),
            json.getInt("defense", 0),
            json.getInt("attack", 1),
            json.getInt("vitality", 1)
        );
    }

    public String toDebugString(Stats maxStats) {
        return "H:" + health + "/" + maxStats.health
            + ",S:" + speed + "/" + maxStats.speed
            + ",AS:" + attackSpeed + "/" + maxStats.attackSpeed
            + ",D:" + defense + "/" + maxStats.defense
            + ",A:" + attack + "/" + maxStats.attack
            + ",V:" + vitality + "/" + maxStats.vitality;
    }
}
