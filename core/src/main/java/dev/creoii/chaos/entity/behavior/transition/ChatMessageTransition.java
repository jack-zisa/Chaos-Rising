package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.ChatManager;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;
import dev.creoii.chaos.util.provider.stringprovider.StringProvider;

public record ChatMessageTransition(StringProvider message, PhaseProvider target) implements Transition {
    public static final MapCodec<ChatMessageTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            StringProvider.CODEC.fieldOf("message").forGetter(ChatMessageTransition::message),
            PhaseProvider.CODEC.fieldOf("target").forGetter(ChatMessageTransition::target)
        ).apply(instance, (message, target) -> new ChatMessageTransition((StringProvider) message.optimize(), (PhaseProvider) target.optimize()));
    });

    @Override
    public Type getType() {
        return Type.THRESHOLD;
    }

    @Override
    public boolean shouldTransition(ContextProvider context, int time) {
        if (context.has(ComponentTypes.WORLD)) {
            ChatManager chatManager = context.get(ComponentTypes.WORLD).getChatManager();
            return chatManager.messages().stream().anyMatch(message1 -> message1.getText().equals(message.get(context)));
        }
        return false;
    }
}
