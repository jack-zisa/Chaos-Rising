package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.network.s2c.ChatMessageReceiveS2C;

public class MessageAction extends Action {
    public static final MapCodec<MessageAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Message.CODEC.fieldOf("message").forGetter(MessageAction::getMessage)
        ).apply(instance, MessageAction::new);
    });
    private final Message message;

    public MessageAction(Message message) {
        this.message = message;
    }

    @Override
    public Type getType() {
        return Type.MESSAGE;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        if (!controller.getEntity().getWorld().getGame().isClient()) {
            controller.getEntity().getWorld().getGame().getServer().sendToAllTCP(new ChatMessageReceiveS2C(message));
        }
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {

    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {

    }

    public Message getMessage() {
        return message;
    }
}
