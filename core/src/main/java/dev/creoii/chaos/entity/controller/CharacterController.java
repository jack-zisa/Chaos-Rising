package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;

public class CharacterController extends EntityController<CharacterEntity> {
    private int attackCooldown;

    public CharacterController(CharacterEntity character) {
        super(character);
        attackCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed));
    }

    @Override
    public void control(float delta) {
        if (getEntity().getGame().getCommandManager().isActive())
            return;

        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(entity.getGame().getInputManager().getKeycode("left"))) dx -= 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getInputManager().getKeycode("right"))) dx += 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getInputManager().getKeycode("up"))) dy += 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getInputManager().getKeycode("down"))) dy -= 1;

        if (dx == 0 && dy == 0) {
            entity.setMoving(false);
            if (!Gdx.input.isTouched()) return;
        }

        Vector2 direction = new Vector2(dx, dy).nor();

        entity.getPos().add(direction.scl(entity.getStats().speed * Entity.COORDINATE_SCALE * delta));
        entity.setMoving(true);

        attack();
    }

    private void attack() {
        if (Gdx.input.isTouched() && --attackCooldown <= 0) {
            BulletEntity bullet = getEntity().getGame().getEntityManager().addEntity(getEntity().getGame().getDataManager().getBullet("test"), new Vector2(entity.getPos()));
            bullet.setParentId(entity.getUuid());

            attackCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed));
        }
    }
}
