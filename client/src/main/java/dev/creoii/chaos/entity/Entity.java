package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.network.packet.util.EntityGroup;

import java.util.UUID;

public class Entity {
    public static final float COORDINATE_SCALE = 32f;
    private final UUID uuid;
    private final EntityGroup group;
    private final Vector2 pos;
    private final Sprite sprite;

    public Entity(ClientGame game, UUID uuid, String textureId, EntityGroup group, float x, float y, float scale) {
        this.uuid = uuid;
        this.group = group;
        pos = new Vector2(x, y);

        String[] texturePath = textureId.split(":");

        sprite = new Sprite(game.getTextureManager().getTexture(texturePath[0], texturePath[1]));
        sprite.setSize(scale, scale);

        if (group == EntityGroup.BULLET) {
            //bullet.sprite.setOriginCenter();
            //sprite.setRotation(bullet.direction.angleDeg() - angleOffset.get(Provider.Context.of(bullet, game.getGametime())));
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public EntityGroup getGroup() {
        return group;
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
