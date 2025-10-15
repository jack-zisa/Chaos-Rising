package dev.creoii.chaos.network.s2c;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.chat.Message;

import java.io.*;

public record ChatMessageReceiveS2C(Message message) {
    public static final Codec<ChatMessageReceiveS2C> CODEC = Message.CODEC.xmap(ChatMessageReceiveS2C::new, ChatMessageReceiveS2C::message);

    public static void write(Output output, ChatMessageReceiveS2C o) {
        output.writeInt(o.message.getSenderId());
        output.writeString(o.message.getText());
        output.writeString(o.message.getColor().toString());
    }

    public static ChatMessageReceiveS2C read(Input input) {
        return new ChatMessageReceiveS2C(new Message(input.readInt(), input.readString(), Color.valueOf(input.readString())));
    }
}
