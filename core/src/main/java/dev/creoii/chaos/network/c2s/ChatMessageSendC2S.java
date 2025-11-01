package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.network.PacketUtils;

public record ChatMessageSendC2S(Message message) {
    public static final Codec<ChatMessageSendC2S> CODEC = Message.OBJECT_CODEC.xmap(ChatMessageSendC2S::new, ChatMessageSendC2S::message);

    public static void write(Output output, ChatMessageSendC2S o) {
        PacketUtils.writeMessage(output, o.message);
    }

    public static ChatMessageSendC2S read(Input input) {
        return new ChatMessageSendC2S(PacketUtils.readMessage(input));
    }
}
