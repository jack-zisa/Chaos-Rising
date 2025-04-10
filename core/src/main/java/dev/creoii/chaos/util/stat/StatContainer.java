package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class StatContainer {
    public final Stat health;
    public final Stat speed;
    public final Stat attackSpeed;
    public final Stat defense;
    public final Stat attack;
    public final Stat vitality;

    public final List<StatModifier> modifiers = new ArrayList<>();

    public StatContainer() {
        this(0, 0, 0, 0, 0, 0);
    }

    public StatContainer(int health, int speed, int attackSpeed, int defense, int attack, int vitality) {
        this.health = new Stat(health);
        this.speed = new Stat(speed);
        this.attackSpeed = new Stat(attackSpeed);
        this.defense = new Stat(defense);
        this.attack = new Stat(attack);
        this.vitality = new Stat(vitality);
    }

    public void applyModifier(StatModifier modifier) {
        modifiers.add(modifier);
        applyModifierToStats(modifier, true);
    }

    public void removeModifier(StatModifier modifier) {
        modifiers.remove(modifier);
        applyModifierToStats(modifier, false);
    }

    private void applyModifierToStats(StatModifier modifier, boolean apply) {
        StatContainer container = modifier.statContainer();
        StatModifier.Type type = modifier.type();

        BiConsumer<Stat, Integer> modify = (stat, amount) -> {
            if (apply)
                stat.addModifier(amount, type);
            else
                stat.removeModifier(amount, type);
        };

        modify.accept(health, container.health.base());
        modify.accept(speed, container.speed.base());
        modify.accept(attackSpeed, container.attackSpeed.base());
        modify.accept(defense, container.defense.base());
        modify.accept(attack, container.attack.base());
        modify.accept(vitality, container.vitality.base());
    }

    public StatContainer copy() {
        return new StatContainer(
            health.base(), speed.base(), attackSpeed.base(),
            defense.base(), attack.base(), vitality.base()
        );
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
            return new StatContainer(jsonValue.getInt("health", 0),
                jsonValue.getInt("speed", 0),
                jsonValue.getInt("attack_speed", 0),
                jsonValue.getInt("defense", 0),
                jsonValue.getInt("attack", 0),
                jsonValue.getInt("vitality", 0)
            );
        }
    }
}
