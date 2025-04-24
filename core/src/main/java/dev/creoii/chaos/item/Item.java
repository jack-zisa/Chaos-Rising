package dev.creoii.chaos.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
    protected final String id;
    protected final String textureId;
    protected final Type type;
    protected final Rarity rarity;
    protected final ItemStack defaultStack;
    protected Sprite sprite;

    public Item(String id, Type type, Rarity rarity, String textureId) {
        this.id = id;
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

    public static Item parse(String id, JsonValue jsonValue) {
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
            return new WeaponItem(id, rarity, textureId, attack, statBonus);
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
            return new ConsumableItem(id, rarity, textureId, statBonus, statusEffects);
        } else if (type == Type.ABILITY) {
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            Attack attack = Attack.parse(jsonValue.get("attack"));
            return new AbilityItem(id, rarity, textureId, statBonus, attack, jsonValue.getInt("cooldown", 0));
        } else if (type == Type.ARMOR || type == Type.ACCESSORY) {
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            return new EquipmentItem(id, type, rarity, textureId, statBonus);
        }
        return new Item(id, type, rarity, textureId);
    }

    public enum Type {
        WEAPON,
        ABILITY,
        ARMOR,
        ACCESSORY,
        CONSUMABLE
    }
}
