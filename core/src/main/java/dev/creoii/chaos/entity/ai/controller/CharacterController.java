package dev.creoii.chaos.entity.ai.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.character.CharacterEntity;

public class CharacterController extends EntityController<CharacterEntity> {
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

        if (input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (input.isKeyPressed(Input.Keys.D)) dx += 1;
        if (input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (input.isKeyPressed(Input.Keys.S)) dy -= 1;

        if (dx == 0 && dy == 0) {
            entity.setMoving(false);
            return;
        }

        Vector2 direction = new Vector2(dx, dy).nor();

        entity.getPos().add(direction.scl(entity.getStats().speed * delta));
        entity.setMoving(true);
    }
}
