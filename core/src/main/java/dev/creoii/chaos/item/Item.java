package dev.creoii.chaos.item;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.util.stat.StatModifier;

public class Item implements DataManager.Identifiable {
    private String id;
    private final Type type;
    private final Attack attack;
    private final StatModifier statModifier;

    public Item(Type type, Attack attack, StatModifier statModifier) {
        this.type = type;
        this.attack = attack;
        this.statModifier = statModifier;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public Attack getAttack() {
        return attack;
    }

    public StatModifier getStatModifier() {
        return statModifier;
    }

    public static class Serializer implements Json.Serializer<Item> {
        @Override
        public void write(Json json, Item item, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", item.id);
            json.writeValue("type", item.type.name().toLowerCase());
            json.writeValue("attack", item.attack);
            json.writeValue("stats", item.statModifier);
            json.writeObjectEnd();
        }

        @Override
        public Item read(Json json, JsonValue jsonValue, Class aClass) {
            Type type = Type.valueOf(jsonValue.getString("type").toUpperCase());
            Attack attack = json.readValue(Attack.class, jsonValue.get("attack"));
            StatModifier stats = jsonValue.has("stat_modifier") ? StatModifier.parse(json, jsonValue.get("stat_modifier")) : StatModifier.NONE;
            return new Item(type, attack, stats);
        }
    }

    public enum Type {
        WEAPON
    }
}
