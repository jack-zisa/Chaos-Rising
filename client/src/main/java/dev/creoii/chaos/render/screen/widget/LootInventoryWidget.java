package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.network.packet.c2s.LootDropCloseC2S;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.render.screen.Screen;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class LootInventoryWidget extends InventoryWidget {
    public LootInventoryWidget(Screen parent, Vector2 pos, Predicate<Main> activePredicate) {
        super(parent, pos, new Slot[2][4], activePredicate);
    }

    @Override
    public Slot[][] getInventory() {
        return getParent().getMain().getGame().getCharacter().getLootInventory();
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (!isActive(manager.getMain()))
            return false;
        if (getParent() instanceof InventoryScreen inventoryScreen) {
            Slot touched = inventoryScreen.getMouseOverSlot();
            if (touched != null && touched.hasItem()) {
                if (!touched.getStack().clickInSlot(manager.getMain().getGame(), touched)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        Main main = manager.getMain();
                        ClientGame game = main.getGame();
                        game.getClient().sendTCP(new SlotUpdateC2S(game.getCharacter().getUuid(), SlotUpdateC2S.Action.QUICK_MOVE, dragSource, touched));

                        if (Arrays.stream(getInventory()).allMatch(Objects::isNull)) {
                            if (game.getCharacter().getLootInventory() != null) {
                                game.getCharacter().clearLootInventory();
                                game.getClient().sendTCP(new LootDropCloseC2S(game.getCharacter().getUuid()));
                            }
                        }
                        return true;
                    }
                    dragSource = touched;
                    dragStack = touched.takeStack();
                }
                return true;
            }
        }
        return false;
    }
}
