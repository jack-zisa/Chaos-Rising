package dev.creoii.chaos.chat;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.util.Optional;

public class Message {
    public static final Codec<Message> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.optionalFieldOf("sender_id").forGetter(message -> message.senderId == -1 ? Optional.empty() : Optional.of(message.senderId)),
            Codec.STRING.fieldOf("text").forGetter(Message::getText),
            Codecs.COLOR.optionalFieldOf("color").forGetter(message -> message.color == Color.WHITE ? Optional.empty() : Optional.of(message.color))
        ).apply(instance, (senderId, text, color) -> new Message(senderId.orElse(-1), text, color.orElse(Color.WHITE)));
    });
    private final int senderId;
    private final String text;
    private final Color color;
    private int cooldown;

    public Message(int senderId, String text, Color color) {
        this.senderId = senderId;
        this.text = text;
        this.color = color;
        cooldown = 1000;
    }

    public Message(int senderId, String text) {
        this(senderId, text, Color.WHITE);
    }

    public Message(String text, Color color) {
        this(-1, text, color);
    }

    public Message(String text) {
        this(-1, text, Color.WHITE);
    }

    public int getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void decrementCooldown() {
        --cooldown;
    }
}
