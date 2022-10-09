package net.teamuni.shootingtest.events;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class PlayerTeleport implements Listener {
    private final ShootingTest main;
    public PlayerTeleport(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        List<String> worlds = main.getConfig().getStringList("enable_world");

        if (worlds.contains(event.getTo().getWorld().getName())) {
            main.getInventory().setPlayerInv(player);
        } else if (worlds.contains(event.getFrom().getWorld().getName())) {
            main.getInventory().returnPlayerInv(player);
        }
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            main.getSetRegion().getPositionMap().clear();
        }
    }
}
