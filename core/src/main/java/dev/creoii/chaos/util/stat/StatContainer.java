package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;
import com.mojang.serialization.Codec;

import java.util.*;

public record StatContainer(Stat health, Stat speed, Stat attackSpeed, Stat defense, Stat attack, Stat vitality) {
    public static final Codec<StatContainer> CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT).xmap(map -> new StatContainer(
        map.getOrDefault(Stat.Type.HEALTH.name().toLowerCase(), 0),
        map.getOrDefault(Stat.Type.SPEED.name().toLowerCase(), 0),
        map.getOrDefault(Stat.Type.ATTACK_SPEED.name().toLowerCase(), 0),
        map.getOrDefault(Stat.Type.DEFENSE.name().toLowerCase(), 0),
        map.getOrDefault(Stat.Type.VITALITY.name().toLowerCase(), 0),
        map.getOrDefault(Stat.Type.ATTACK.name().toLowerCase(), 0)
    ), statContainer -> {
        Map<String, Integer> map = new HashMap<>();
        map.put(Stat.Type.HEALTH.name().toLowerCase(), statContainer.health.value());
        map.put(Stat.Type.SPEED.name().toLowerCase(), statContainer.speed.value());
        map.put(Stat.Type.ATTACK_SPEED.name().toLowerCase(), statContainer.attackSpeed.value());
        map.put(Stat.Type.DEFENSE.name().toLowerCase(), statContainer.defense.value());
        map.put(Stat.Type.VITALITY.name().toLowerCase(), statContainer.vitality.value());
        map.put(Stat.Type.ATTACK.name().toLowerCase(), statContainer.attack.value());
        return map;
    });

    public StatContainer() {
        this(0, 0, 0, 0, 0, 0);
    }

    public StatContainer(int health, int speed, int attackSpeed, int defense, int attack, int vitality) {
        this(new Stat(Stat.Type.HEALTH, health), new Stat(Stat.Type.SPEED, speed), new Stat(Stat.Type.ATTACK_SPEED, attackSpeed), new Stat(Stat.Type.DEFENSE, defense), new Stat(Stat.Type.ATTACK, attack), new Stat(Stat.Type.VITALITY, vitality));
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

    public static StatContainer parse(JsonValue jsonValue) {
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
