package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.*;

public class StatContainer {
    public final Stat health;
    public final Stat speed;
    public final Stat attackSpeed;
    public final Stat defense;
    public final Stat attack;
    public final Stat vitality;

    public StatContainer(int health, int speed, int attackSpeed, int defense, int attack, int vitality) {
        this(new Stat(Stat.Type.HEALTH, health), new Stat(Stat.Type.SPEED, speed), new Stat(Stat.Type.ATTACK_SPEED, attackSpeed), new Stat(Stat.Type.DEFENSE, defense), new Stat(Stat.Type.ATTACK, attack), new Stat(Stat.Type.VITALITY, vitality));
    }

    public StatContainer(Stat health, Stat speed, Stat attackSpeed, Stat defense, Stat attack, Stat vitality) {
        this.health = health;
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.defense = defense;
        this.attack = attack;
        this.vitality = vitality;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed.set(attackSpeed);
    }

    public void setDefense(int defense) {
        this.defense.set(defense);
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public void setVitality(int vitality) {
        this.vitality.set(vitality);
    }

    public void set(StatContainer other) {
        setHealth(other.health.base());
        setSpeed(other.speed.base());
        setAttackSpeed(other.attackSpeed.base());
        setDefense(other.defense.base());
        setAttack(other.attack.base());
        setVitality(other.vitality.base());
    }

    public StatContainer copy() {
        return new StatContainer(
            health.base(), speed.base(), attackSpeed.base(),
            defense.base(), attack.base(), vitality.base()
        );
    }

    public static String getTooltip(List<ModifierEntry> modifierEntries) {
        List<String> lines = new ArrayList<>();

        for (ModifierEntry modifierEntry : modifierEntries) {
            StringBuilder builder = new StringBuilder();
            builder.append(modifierEntry.operation().getPrefix()).append(modifierEntry.amount()).append(" ").append(modifierEntry.type().name().toLowerCase());
            lines.add(builder.toString());
        }

        return String.join("\n", lines);
    }

    public String toDebugString(StatContainer maxStatContainer) {
        return "H:" + health + "/" + maxStatContainer.health
            + ",S:" + speed + "/" + maxStatContainer.speed
            + ",AS:" + attackSpeed + "/" + maxStatContainer.attackSpeed
            + ",D:" + defense + "/" + maxStatContainer.defense
            + ",A:" + attack + "/" + maxStatContainer.attack
            + ",V:" + vitality + "/" + maxStatContainer.vitality;
    }

    public static class Serializer implements Json.Serializer<StatContainer> {
        @Override
        public void write(Json json, StatContainer statContainer, Class knownType) {
            json.writeObjectStart();
            json.writeValue("health", statContainer.health);
            json.writeValue("speed", statContainer.speed);
            json.writeValue("attack_speed", statContainer.attackSpeed);
            json.writeValue("defense", statContainer.defense);
            json.writeValue("attack", statContainer.attack);
            json.writeValue("vitality", statContainer.vitality);
            json.writeObjectEnd();
        }

        @Override
        public StatContainer read(Json json, JsonValue jsonValue, Class aClass) {
            return new StatContainer(
                new Stat(Stat.Type.HEALTH, jsonValue.getInt("health", 0)),
                new Stat(Stat.Type.SPEED, jsonValue.getInt("speed", 0)),
                new Stat(Stat.Type.ATTACK_SPEED, jsonValue.getInt("attack_speed", 0)),
                new Stat(Stat.Type.DEFENSE, jsonValue.getInt("defense", 0)),
                new Stat(Stat.Type.ATTACK, jsonValue.getInt("attack", 0)),
                new Stat(Stat.Type.VITALITY, jsonValue.getInt("vitality", 0))
            );
        }
    }
}
