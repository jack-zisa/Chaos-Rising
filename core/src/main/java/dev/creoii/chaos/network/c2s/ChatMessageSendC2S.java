package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.chat.Message;

import java.io.Serializable;

public record ChatMessageSendC2S(Message message) implements Serializable {
    public static final Codec<ChatMessageSendC2S> CODEC = Message.CODEC.xmap(ChatMessageSendC2S::new, ChatMessageSendC2S::message);
}
