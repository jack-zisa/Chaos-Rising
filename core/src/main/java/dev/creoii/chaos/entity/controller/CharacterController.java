package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;

public class CharacterController extends EntityController<CharacterEntity> {
    private int attackCooldown = 20;

    public CharacterController(CharacterEntity character) {
        super(character);
    }

    @Override
    public void control(float delta) {
        if (getEntity().getGame().getCommandManager().isActive())
            return;

        Input input = Gdx.input;

        float dx = 0f;
        float dy = 0f;

        if (input.isKeyPressed(entity.getGame().getInputManager().getKeycode("left"))) dx -= 1;
        if (input.isKeyPressed(entity.getGame().getInputManager().getKeycode("right"))) dx += 1;
        if (input.isKeyPressed(entity.getGame().getInputManager().getKeycode("up"))) dy += 1;
        if (input.isKeyPressed(entity.getGame().getInputManager().getKeycode("down"))) dy -= 1;

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
            BulletEntity bullet = getEntity().getGame().getEntityManager().addEntity(getEntity().getGame().getDataManager().getBullet("test"), new Vector2(entity.getCenterPos()));
            bullet.setParentId(entity.getUuid());

            attackCooldown = 20;
        }
    }
}
