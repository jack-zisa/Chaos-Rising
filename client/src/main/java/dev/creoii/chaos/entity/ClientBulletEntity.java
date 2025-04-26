package dev.creoii.chaos.entity;

import dev.creoii.chaos.ClientGame;

import java.util.UUID;

public class ClientBulletEntity extends ClientEntity {
    public ClientBulletEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale, float xDir, float yDir) {
        super(game, uuid, textureId, x, y, scale);
        float angle = (float) Math.atan2(yDir, xDir) * (180f / (float) Math.PI) % 360f;
        getSprite().setOriginCenter();
        getSprite().setRotation(angle/* - angleOffset.get(Provider.Context.of(bullet, game.getGametime()))*/);
    }
}
