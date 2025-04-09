package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;

import java.util.HashMap;
import java.util.Map;

public class CharacterController extends EntityController<CharacterEntity> {
    private int attackCooldown;

    public CharacterController(CharacterEntity character) {
        super(character);
        attackCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed));
    }

    @Override
    public void control(int gametime, float delta) {
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

        if (entity.getCurrentItem() != null)
            attack();
    }

    private void attack() {
        if (Gdx.input.isTouched() && --attackCooldown <= 0) {
            Vector3 mousePos = getEntity().getGame().getInputManager().getMousePos();
            Vector2 direction = new Vector2(mousePos.x, mousePos.y).sub(entity.getCenterPos()).nor();

            int bulletCount = entity.getCurrentItem().bulletCount();
            int arcGap = entity.getCurrentItem().arcGap();
            float baseAngle = -arcGap * (bulletCount - 1) / 2f;

            for (int i = 0; i < bulletCount; i++) {
                float angle = baseAngle + i * arcGap;

                Map<String, Object> customData = new HashMap<>();
                customData.put("direction", direction.cpy().rotateDeg(angle));
                customData.put("damage", entity.getCurrentItem().damage());

                BulletEntity bullet = getEntity().getGame().getEntityManager().addEntity(getEntity().getGame().getDataManager().getBullet(entity.getCurrentItem().bulletId()), new Vector2(entity.getPos()), customData);
                bullet.setParentId(entity.getUuid());
                bullet.setIndex(i % 2 == 0 ? 1 : -1);
            }
            attackCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed));
        }
    }
}
