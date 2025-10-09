package dev.creoii.chaos.inventory.cooldown;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;
import dev.creoii.chaos.util.Tickable;

import java.util.*;

public class CooldownManager implements Tickable {
    private final ServerGame game;
    private final Map<UUID, List<Cooldown>> playerSlotCooldowns;

    public CooldownManager(ServerGame game) {
        this.game = game;
        playerSlotCooldowns = new HashMap<>();
        //game.getTickManager().addTickable(this);
    }

    public ServerGame getGame() {
        return game;
    }

    public void addCooldown(UUID uuid, int ri, int ci, int cooldown) {
        playerSlotCooldowns.computeIfAbsent(uuid, _ -> new ArrayList<>()).add(new Cooldown(ri, ci, new Mutable<>(cooldown)));
    }

    @Override
    public void tick(int gametime, float delta) {
        playerSlotCooldowns.forEach((uuid, cooldowns) -> {
            Iterator<Cooldown> it = cooldowns.iterator();
            while (it.hasNext()) {
                Cooldown cooldown = it.next();

                cooldown.cooldown.set(cooldown.cooldown.get() - 1);

                if (cooldown.cooldown.get() <= 0) {
                    ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, uuid)).getInventory().getSlot(cooldown.ri, cooldown.ci).setActive(false);
                    it.remove();
                } else ((CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, uuid)).getInventory().getSlot(cooldown.ri, cooldown.ci).setActive(true);
            }
        });
    }

    public record Cooldown(int ri, int ci, Mutable<Integer> cooldown) {}
}
