package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;

import java.util.UUID;

public class ClientEntity {
    public static final float COORDINATE_SCALE = 32f;
    private final UUID uuid;
    private final Vector2 pos;
    private final Sprite sprite;

    public ClientEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale) {
        this.uuid = uuid;
        pos = new Vector2(x, y);

        String[] texturePath = textureId.split(":");

        sprite = new Sprite(game.getTextureManager().getTexture(texturePath[0], texturePath[1]));
        sprite.setSize(scale, scale);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Vector2 getPos() {
        sprite.setPosition(pos.x, pos.y);
        return pos;
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
