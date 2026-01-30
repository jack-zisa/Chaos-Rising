package dev.creoii.chaos.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.effect.StatusEffects;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.Stat;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;

public final class PacketUtils {
    @Nullable
    public static Item readItem(Input input) {
        String id = input.readString();
        return DataManager.getItem(id);
    }

    public static void writeItem(Output output, Item item) {
        output.writeString(item != null ? item.id() : "");
    }

    public static ItemStack readItemStack(Input input) {
        Item item = readItem(input);
        int count = input.readInt();
        if (item == null)
            return ItemStack.EMPTY;
        return new ItemStack(item, count);
    }

    public static void writeItemStack(Output output, ItemStack stack) {
        writeItem(output, stack.getItem());
        output.writeInt(stack.getCount());
    }

    public static Message readMessage(Input input) {
        return new Message(input.readInt(), input.readString(), readColor(input));
    }

    public static void writeMessage(Output output, Message message) {
        output.writeInt(message.getSenderId());
        output.writeString(message.getText());
        writeColor(output, message.getColor());
    }

    public static Color readColor(Input input) {
        return Color.valueOf(input.readString());
    }

    public static void writeColor(Output output, Color color) {
        output.writeString(color.toString());
    }

    public static <E extends Enum<E>> E readEnum(Class<E> enumClass, Input input) {
        int ordinal = input.readVarInt(true);
        E[] constants = enumClass.getEnumConstants();
        if (ordinal < 0 || ordinal >= constants.length)
            throw new IllegalArgumentException("Invalid ordinal " + ordinal + " for enum " + enumClass.getSimpleName());
        return constants[ordinal];
    }

    public static <E extends Enum<E>> void writeEnum(Output output, E e) {
        output.writeVarInt(e.ordinal(), true);
    }

    public static Slot readSlot(Input input) {
        return new Slot(input.readInt(), input.readInt(), readEnum(Slot.Type.class, input), readItemStack(input));
    }

    public static void writeSlot(Output output, Slot slot) {
        output.writeInt(slot.getR());
        output.writeInt(slot.getC());
        writeEnum(output, slot.getType());
        writeItemStack(output, slot.getStack());
    }

    public static Stat readStat(Input input) {
        return new Stat(readEnum(Stat.Type.class, input), input.readInt());
    }

    public static void writeStat(Output output, Stat stat) {
        writeEnum(output, stat.type());
        output.writeInt(stat.value());
    }

    public static void writeStatContainer(Output output, StatContainer container) {
        writeStat(output, container.health());
        writeStat(output, container.speed());
        writeStat(output, container.attackSpeed());
        writeStat(output, container.defense());
        writeStat(output, container.attack());
        writeStat(output, container.vitality());
    }

    public static StatContainer readStatContainer(Input input) {
        return new StatContainer(readStat(input), readStat(input), readStat(input), readStat(input), readStat(input), readStat(input));
    }

    public static void writeStatContainerFast(Output output, StatContainer container) {
        output.writeInt(container.health().value());
        output.writeInt(container.speed().value());
        output.writeInt(container.attackSpeed().value());
        output.writeInt(container.defense().value());
        output.writeInt(container.attack().value());
        output.writeInt(container.vitality().value());
    }

    public static StatContainer readStatContainerFast(Input input) {
        return new StatContainer(input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readInt());
    }

    public static void writeCustomEntityData(Output output, EntityCustomData customData) {
        writeEnum(output, customData.getGroup());
        customData.write(output);
    }

    public static EntityCustomData readCustomEntityData(Input input) {
        EntityGroup group = readEnum(EntityGroup.class, input);
        return switch (group) {
            case CHARACTER -> CharacterData.read(input);
            case ENEMY, OBJECT -> EnemyData.read(input);
            case BULLET -> BulletData.read(input);
            case LOOT_DROP -> LootDropData.read(input);
        };
    }

    public static void writeStatusEffectInstance(Output output, StatusEffect.Instance instance) {
        writeEnum(output, instance.getEffect().type());
        output.writeInt(instance.getAmplifier());
        output.writeInt(instance.getDuration());
    }

    public static StatusEffect.Instance readStatusEffectInstance(Input input) {
        StatusEffect effect = StatusEffects.ALL.get(readEnum(StatusEffect.Type.class, input));
        return new StatusEffect.Instance(effect, input.readInt(), input.readInt());
    }
}
