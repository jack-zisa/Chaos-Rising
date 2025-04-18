package dev.creoii.chaos.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.effect.StatusEffects;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatContainer;
import dev.creoii.chaos.util.stat.StatModifier;

import java.util.ArrayList;
import java.util.List;

public class Item implements DataManager.Identifiable {
    protected String id;
    protected final String textureId;
    protected final Type type;
    protected final Rarity rarity;
    protected Sprite sprite;
    protected final ItemStack defaultStack;

    public Item(Type type, Rarity rarity, String textureId) {
        this.type = type;
        this.rarity = rarity;
        this.textureId = textureId;
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

    public Sprite getSprite() {
        return sprite;
    }

    public ItemStack getDefaultStack() {
        return defaultStack;
    }

    public boolean clickInSlot(InputManager manager, Slot slot, ItemStack stack) {
        return false;
    }

    public String getTooltip() {
        return id + "\n";
    }

    public static class Serializer implements Json.Serializer<Item> {
        @Override
        public void write(Json json, Item item, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", item.id);
            json.writeValue("type", item.type.name().toLowerCase());
            json.writeValue("rarity", item.rarity.name().toLowerCase());
            json.writeObjectEnd();
        }

        @Override
        public Item read(Json json, JsonValue jsonValue, Class aClass) {
            Type type = Type.valueOf(jsonValue.getString("type").toUpperCase());
            Rarity rarity = jsonValue.has("rarity") ? Rarity.valueOf(jsonValue.getString("rarity").toUpperCase()) : Rarity.COMMON;
            String textureId = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);

            if (type == Type.WEAPON) {
                Attack attack = Attack.parse(jsonValue.get("attack"));
                // replace NONE with null
                StatModifier stats = jsonValue.has("stat_modifier") ? StatModifier.parse(json, jsonValue.get("stat_modifier")) : StatModifier.NONE;
                return new WeaponItem(rarity, textureId, attack, stats);
            } else if (type == Type.CONSUMABLE) {
                StatContainer statContainer = jsonValue.has("stat_bonus") ? json.readValue(StatContainer.class, jsonValue.get("stat_bonus")) : null;
                List<StatusEffect> statusEffects = new ArrayList<>();
                if (jsonValue.has("status_effects")) {
                    jsonValue.get("status_effects").forEach(effectValue -> {
                        StatusEffect statusEffect = StatusEffects.ALL.get(effectValue.getString("id"));
                        statusEffect.init(effectValue.getInt("amplifier", 0), effectValue.getInt("duration", 0));
                        statusEffects.add(statusEffect);
                    });
                }
                return new ConsumableItem(rarity, textureId, statContainer, statusEffects);
            } else if (type == Type.ABILITY) {
                StatModifier stats = jsonValue.has("stat_modifier") ? StatModifier.parse(json, jsonValue.get("stat_modifier")) : StatModifier.NONE;
                Attack attack = Attack.parse(jsonValue.get("attack"));
                return new AbilityItem(rarity, textureId, stats, attack, Attack.Target.valueOf(jsonValue.getString("target", Attack.Target.MOUSE_POS.name()).toUpperCase()), jsonValue.getInt("cooldown", 0));
            } else if (type == Type.ARMOR || type == Type.ACCESSORY) {
                StatModifier stats = jsonValue.has("stat_modifier") ? StatModifier.parse(json, jsonValue.get("stat_modifier")) : StatModifier.NONE;
                return new EquipmentItem(type, rarity, textureId, stats);
            }
            return new Item(type, rarity, textureId);
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
