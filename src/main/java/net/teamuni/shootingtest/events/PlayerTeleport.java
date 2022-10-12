package net.teamuni.shootingtest.events;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {
    private final ShootingTest main;
    public PlayerTeleport(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
        if (main.getSetRegion().getPositionMap().isEmpty()) return;
        main.getSetRegion().getPositionMap().clear();
    }
}
