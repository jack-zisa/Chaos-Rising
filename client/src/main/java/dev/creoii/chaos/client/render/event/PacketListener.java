package dev.creoii.chaos.client.render.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class PacketListener implements EventListener {
    public boolean handle(Event event) {
        if (event instanceof PacketEvent packetEvent) {
            received(packetEvent, event.getTarget());
        }
        return false;
    }

    public abstract void received(PacketEvent event, Actor actor);

    public static class PacketEvent extends Event {
        private Object packet;

        public void setPacket(Object packet) {
            this.packet = packet;
        }

        public Object getPacket() {
            return packet;
        }
    }
}
