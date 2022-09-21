package net.teamuni.shootingtest.events;

import net.teamuni.shootingtest.PlayerInv;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {
    private final ShootingTest main = ShootingTest.getInstance();
    private final PlayerInv inv = PlayerInv.getPlayerInv();

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getWorld().getName().equals(main.getConfig().getString("range.world"))) {
            inv.setPlayerInv(player);
        } else if (event.getFrom().getWorld().getName().equals(main.getConfig().getString("range.world"))) {
            inv.returnPlayerInv(player);
        }
    }
}
