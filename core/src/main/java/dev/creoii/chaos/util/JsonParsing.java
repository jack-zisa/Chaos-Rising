package dev.creoii.chaos.util;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.effect.StatusEffectType;
import dev.creoii.chaos.effect.StatusEffectTypes;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.bulletpath.BulletPath;
import dev.creoii.chaos.item.*;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public final class JsonParsing {
    public static Identifiable parseItem(String id, JsonValue jsonValue) {
        Item.Type type = Item.Type.valueOf(jsonValue.getString("type").toUpperCase());
        Rarity rarity = jsonValue.has("rarity") ? Rarity.valueOf(jsonValue.getString("rarity").toUpperCase()) : Rarity.COMMON;

        if (type == Item.Type.WEAPON) {
            Attack attack = Attack.parse(jsonValue.get("attack"));
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            return new WeaponItem(id, rarity, attack, statBonus);
        } else if (type == Item.Type.CONSUMABLE) {
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            List<StatusEffect> statusEffects = new ArrayList<>();
            if (jsonValue.has("status_effects")) {
                jsonValue.get("status_effects").forEach(effectValue -> {
                    StatusEffectType statusEffectType = StatusEffectTypes.ALL.get(effectValue.getString("id"));
                    statusEffects.add(new StatusEffect(statusEffectType, effectValue.getInt("amplifier", 0), effectValue.getInt("duration", 0)));
                });
            }
            return new ConsumableItem(id, rarity, statBonus, statusEffects);
        } else if (type == Item.Type.ABILITY) {
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            Attack attack = Attack.parse(jsonValue.get("attack"));
            return new AbilityItem(id, rarity, statBonus, attack, jsonValue.getInt("cooldown", 0));
        } else if (type == Item.Type.ARMOR || type == Item.Type.ACCESSORY) {
            List<ModifierEntry> statBonus = new ArrayList<>();
            if (jsonValue.has("stat_bonus")) {
                jsonValue.get("stat_bonus").forEach(modifierValue -> {
                    statBonus.add(ModifierEntry.parse(modifierValue));
                });
            }
            return new EquipmentItem(id, type, rarity, statBonus);
        }
        return new Item(id, type, rarity);
    }

    public static CharacterClass parseCharacterClass(String id, JsonValue jsonValue) {
        float scale = jsonValue.getFloat("scale", 1f);
        StatContainer baseStatContainer = StatContainer.parse(jsonValue.get("base_stats"));
        StatContainer maxStatContainer = StatContainer.parse(jsonValue.get("max_stats"));
        return new CharacterClass(id, scale, baseStatContainer, maxStatContainer);
    }

    public static BulletEntityType parseBulletEntityType(String id, JsonValue jsonValue) {
        float scale = jsonValue.getFloat("scale", 1f) * Entity.COORDINATE_SCALE;
        int lifetime = jsonValue.getInt("lifetime", 0);
        NumberProvider angleOffset = NumberProvider.parse(jsonValue.get("angle_offset"), 45);
        BulletPath bulletPath = BulletPath.parse(jsonValue);
        BooleanProvider piercing = BooleanProvider.parse(jsonValue.get("piercing"), false);
        return new BulletEntityType(id, scale, lifetime, angleOffset, bulletPath, piercing);
    }

    public static EnemyEntityType parseEnemyEntityType(String id, JsonValue jsonValue) {
        float scale = jsonValue.getFloat("scale", 1f) * Entity.COORDINATE_SCALE;
        StatContainer statContainer = jsonValue.has("stats") ? StatContainer.parse(jsonValue.get("stats")) : EnemyEntityType.DEFAULT_STAT_CONTAINER.copy();
        LootTable lootTable = jsonValue.has("loot_table") ? LootTable.parse(jsonValue.get("loot_table")) : null;
        if (jsonValue.has("behavior")) {
            Behavior behavior = Behavior.parse(jsonValue.get("behavior"));
            return new EnemyEntityType(id, scale, lootTable, behavior, statContainer);
        }
        return new EnemyEntityType(id, scale, lootTable, null, statContainer);
    }

    public static LootDropEntityType parseLootDropEntityType(String id, JsonValue jsonValue) {
        float scale = jsonValue.getFloat("scale", 1f) * Entity.COORDINATE_SCALE;
        BooleanProvider removeEmpty = BooleanProvider.parse(jsonValue.get("remove_empty"), false);
        return new LootDropEntityType(id, scale, removeEmpty);
    }
}
