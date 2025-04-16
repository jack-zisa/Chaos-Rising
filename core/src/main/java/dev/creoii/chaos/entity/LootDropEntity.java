package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;

import java.util.UUID;

public class LootDropEntity extends Entity {
    private final Inventory inventory;

    public LootDropEntity(String textureId, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.OTHER);
        inventory = new Inventory(2, 4);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        LootDropEntity entity = new LootDropEntity(getTextureId(), 1f);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("loot", getTextureId()));
        entity.sprite.setSize(getScale(), getScale());
        return entity;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (gametime - getSpawnTime() >= 3600)
            remove();
    }

    @Override
    public EntityController<?> getController() {
        return null;
    }

    @Override
    public void collisionEnter(Entity other) {

    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {
    }
}
