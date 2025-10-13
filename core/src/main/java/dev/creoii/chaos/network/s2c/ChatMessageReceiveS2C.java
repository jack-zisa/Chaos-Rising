package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.chat.Message;

import java.io.Serializable;

public record ChatMessageReceiveS2C(Message message) implements Serializable {
    public static final Codec<ChatMessageReceiveS2C> CODEC = Message.CODEC.xmap(ChatMessageReceiveS2C::new, ChatMessageReceiveS2C::message);
}
