package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.character.CharacterClass;

public class Stats {
    public int health;
    public int speed;
    public int attackSpeed;
    public int defense;
    public int attack;
    public int vitality;

    public Stats() {
        this(10, 1, 1, 0, 1, 1);
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

    public static class Serializer implements Json.Serializer<Stats> {
        @Override
        public void write(Json json, Stats stats, Class knownType) {
            json.writeObjectStart();
            json.writeValue("health", stats.health);
            json.writeValue("speed", stats.speed);
            json.writeValue("attack_speed", stats.attackSpeed);
            json.writeValue("defense", stats.defense);
            json.writeValue("attack", stats.attack);
            json.writeValue("vitality", stats.vitality);
            json.writeObjectEnd();
        }

        @Override
        public Stats read(Json json, JsonValue jsonValue, Class aClass) {
            return new Stats(jsonValue.getInt("health", 1),
                jsonValue.getInt("speed", 1),
                jsonValue.getInt("attack_speed", 1),
                jsonValue.getInt("defense", 0),
                jsonValue.getInt("attack", 1),
                jsonValue.getInt("vitality", 1)
            );
        }
    }
}
