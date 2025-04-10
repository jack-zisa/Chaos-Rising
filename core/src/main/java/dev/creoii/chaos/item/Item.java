package dev.creoii.chaos.item;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.attack.Attack;

public class Item implements DataManager.Identifiable {
    private String id;
    private final Type type;
    private final Attack attack;

    public Item(Type type, Attack attack) {
        this.type = type;
        this.attack = attack;
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

    public static class Serializer implements Json.Serializer<Item> {
        @Override
        public void write(Json json, Item item, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", item.id);
            json.writeValue("type", item.type.name().toLowerCase());
            json.writeValue("attack", item.attack);
            json.writeObjectEnd();
        }

        @Override
        public Item read(Json json, JsonValue jsonValue, Class aClass) {
            Type type = Type.valueOf(jsonValue.getString("type").toUpperCase());
            Attack attack = json.readValue(Attack.class, jsonValue.get("attack"));
            return new Item(type, attack);
        }
    }

    public enum Type {
        WEAPON
    }
}
