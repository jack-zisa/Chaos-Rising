package dev.creoii.chaos.entity.ai.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        boolean[] keys = new boolean[4];

        keys[0] = input.isKeyPressed(Input.Keys.A);
        keys[1] = input.isKeyPressed(Input.Keys.W);
        keys[2] = input.isKeyPressed(Input.Keys.S);
        keys[3] = input.isKeyPressed(Input.Keys.D);

        if (!keys[0] && !keys[1] && !keys[2] && !keys[3]) {
            entity.setMoving(false);
        } else {
            int speed = 1;
            if (keys[0]) {
                entity.getPos().x = entity.getPos().x - speed * delta;
                entity.setMoving(true);
            }
            if (keys[1]) {
                entity.getPos().y = entity.getPos().y + speed * delta;
                entity.setMoving(true);
            }
            if (keys[2]) {
                entity.getPos().y = entity.getPos().y - speed * delta;
                entity.setMoving(true);
            }
            if (keys[3]) {
                entity.getPos().x = entity.getPos().x + speed * delta;
                entity.setMoving(true);
            }
        }
    }
}
