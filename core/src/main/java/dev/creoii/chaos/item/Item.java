package dev.creoii.chaos.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatModifier;

public class Item implements DataManager.Identifiable {
    protected String id;
    protected final String textureId;
    protected final Type type;
    protected final Rarity rarity;
    protected final StatModifier statModifier;
    protected Sprite sprite;
    protected final ItemStack defaultStack;

    public Item(Type type, Rarity rarity, String textureId, StatModifier statModifier) {
        this.type = type;
        this.rarity = rarity;
        this.textureId = textureId;
        this.statModifier = statModifier;
        defaultStack = new ItemStack(this);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onLoad(Main main) {
        sprite = new Sprite(main.getGame().getTextureManager().getTexture("item", textureId));
    }

    public Type getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public StatModifier getStatModifier() {
        return statModifier;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public ItemStack getDefaultStack() {
        return defaultStack;
    }

    public boolean clickInSlot(InputManager manager, Slot slot, ItemStack stack) {
        return false;
    }

    public static class Serializer implements Json.Serializer<Item> {
        @Override
        public void write(Json json, Item item, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", item.id);
            json.writeValue("type", item.type.name().toLowerCase());
            json.writeValue("rarity", item.rarity.name().toLowerCase());
            json.writeValue("stats", item.statModifier);
            json.writeObjectEnd();
        }

        @Override
        public Item read(Json json, JsonValue jsonValue, Class aClass) {
            Type type = Type.valueOf(jsonValue.getString("type").toUpperCase());
            Rarity rarity = jsonValue.has("rarity") ? Rarity.valueOf(jsonValue.getString("rarity").toUpperCase()) : Rarity.COMMON;
            String textureId = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            // replace NONE with null
            StatModifier stats = jsonValue.has("stat_modifier") ? StatModifier.parse(json, jsonValue.get("stat_modifier")) : StatModifier.NONE;

            if (type == Type.WEAPON) {
                Attack attack = Attack.parse(jsonValue.get("attack"));
                return new WeaponItem(rarity, textureId, attack, stats);
            } else if (type == Type.CONSUMABLE) {
                return new ConsumableItem(rarity, textureId);
            }

            return new Item(type, rarity, textureId, stats);
        }
    }

    public enum Type {
        WEAPON,
        ABILITY,
        ARMOR,
        ACCESSORY,
        CONSUMABLE
    }
}
