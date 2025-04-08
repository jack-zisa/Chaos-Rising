package dev.creoii.chaos.item;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;

public record Item(String id, String bulletId, int damage, int bulletCount, int arcGap) implements DataManager.Identifiable {
    public static class Serializer implements Json.Serializer<Item> {
        @Override
        public void write(Json json, Item item, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", item.id);
            json.writeValue("bullet_id", item.bulletId);
            json.writeValue("damage", item.damage);
            json.writeValue("bullet_count", item.bulletCount);
            json.writeValue("arc_gap", item.arcGap);
            json.writeObjectEnd();
        }

        @Override
        public Item read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            String bulletId = jsonValue.getString("bullet_id");
            int damage = jsonValue.getInt("damage", 0);
            int bulletCount = jsonValue.getInt("bullet_count", 1);
            int arcGap = jsonValue.getInt("arc_gap", 0);
            return new Item(id, bulletId, damage, bulletCount, arcGap);
        }
    }
}
