package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.UUID;

public class BulletEntity extends Entity {
    private Entity parent;
    protected Vector2 direction;
    protected int lifetime;
    protected int damage;
    private int index;

    public BulletEntity(Game game, UUID uuid, Vector2 pos, float scale, Vector2 direction, NumberProvider angleOffset) {
        super(game, uuid, pos, scale);
        //float angle = (float) Math.atan2(yDir, xDir) * (180f / (float) Math.PI) % 360f;
        //getSprite().setOriginCenter();
        //getSprite().setRotation(angle - angleOffset.get(Provider.Context.of(this, game.getGametime())));
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
