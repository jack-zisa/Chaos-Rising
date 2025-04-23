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
import dev.creoii.chaos.util.stat.ModifierEntry;

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
                List<ModifierEntry> statBonus = new ArrayList<>();
                if (jsonValue.has("stat_bonus")) {
                    jsonValue.get("stat_bonus").forEach(modifierValue -> {
                        statBonus.add(ModifierEntry.parse(modifierValue));
                    });
                }
                return new WeaponItem(rarity, textureId, attack, statBonus);
            } else if (type == Type.CONSUMABLE) {
                List<ModifierEntry> statBonus = new ArrayList<>();
                if (jsonValue.has("stat_bonus")) {
                    jsonValue.get("stat_bonus").forEach(modifierValue -> {
                        statBonus.add(ModifierEntry.parse(modifierValue));
                    });
                }
                List<StatusEffect> statusEffects = new ArrayList<>();
                if (jsonValue.has("status_effects")) {
                    jsonValue.get("status_effects").forEach(effectValue -> {
                        StatusEffect statusEffect = StatusEffects.ALL.get(effectValue.getString("id"));
                        statusEffect.init(effectValue.getInt("amplifier", 0), effectValue.getInt("duration", 0));
                        statusEffects.add(statusEffect);
                    });
                }
                return new ConsumableItem(rarity, textureId, statBonus, statusEffects);
            } else if (type == Type.ABILITY) {
                List<ModifierEntry> statBonus = new ArrayList<>();
                if (jsonValue.has("stat_bonus")) {
                    jsonValue.get("stat_bonus").forEach(modifierValue -> {
                        statBonus.add(ModifierEntry.parse(modifierValue));
                    });
                }
                Attack attack = Attack.parse(jsonValue.get("attack"));
                return new AbilityItem(rarity, textureId, statBonus, attack, jsonValue.getInt("cooldown", 0));
            } else if (type == Type.ARMOR || type == Type.ACCESSORY) {
                List<ModifierEntry> statBonus = new ArrayList<>();
                if (jsonValue.has("stat_bonus")) {
                    jsonValue.get("stat_bonus").forEach(modifierValue -> {
                        statBonus.add(ModifierEntry.parse(modifierValue));
                    });
                }
                return new EquipmentItem(type, rarity, textureId, statBonus);
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
