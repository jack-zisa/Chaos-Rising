package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.inventory.Inventory;
import dev.creoii.chaos.loot.LootEntry;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.LootUtils;

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

        LootTable lootTable = new LootTable();
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test").create(game), 4, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_three").create(game), 1, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_multi").create(game), 1, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_random").create(game), 1, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_spread").create(game), 1, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_circle").create(game), 1, 1, 1));
        lootTable.addEntry(new LootEntry(game.getDataManager().getItem("test_backwards").create(game), 1, 1, 1));

        LootUtils.insertIntoInventory(inventory, lootTable, 3);

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
