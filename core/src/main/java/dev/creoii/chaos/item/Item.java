package dev.creoii.chaos.item;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.attack.Attack;

public record Item(String id, Type type, Attack attack) implements DataManager.Identifiable {
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
            String id = jsonValue.getString("id");
            Type type = Type.valueOf(jsonValue.getString("type").toUpperCase());
            Attack attack = json.readValue(Attack.class, jsonValue.get("attack"));
            return new Item(id, type, attack);
        }
    }

    public enum Type {
        WEAPON
    }
}
