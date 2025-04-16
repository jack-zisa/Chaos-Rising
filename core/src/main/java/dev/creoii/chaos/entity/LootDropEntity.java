package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;

import java.util.UUID;

public class LootDropEntity extends Entity {
    private Inventory inventory;

    public LootDropEntity(String textureId, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.OTHER);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        LootDropEntity entity = new LootDropEntity(getTextureId(), 1f);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("loot", getTextureId()));
        entity.sprite.setSize(getScale(), getScale());
        Inventory inventory = new Inventory(2, 4);
        inventory.addItem(game.getDataManager().getItem("test").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_three").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_multi").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_random").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_spread").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_circle").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test_backwards").create(game).getDefaultStack());
        inventory.addItem(game.getDataManager().getItem("test").create(game).getDefaultStack());
        entity.inventory = inventory;
        return entity;
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
