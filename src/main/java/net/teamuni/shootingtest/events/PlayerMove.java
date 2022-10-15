package net.teamuni.shootingtest.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    private final ShootingTest main;

    public PlayerMove(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;
        Player player = event.getPlayer();
        for (CuboidRegion cuboid : main.getRegionManager().getRegion().values()) {
            if (!cuboid.getWorld().equals(BukkitAdapter.adapt(player.getWorld()))) continue;
            if (isInRegion(cuboid, event.getTo()) && !isInRegion(cuboid, event.getFrom())) {
                if (main.getItemManager().hasMenuItem(player)) continue;
                main.getInventory().setPlayerInv(player);
            } else if (isInRegion(cuboid, event.getFrom()) && !isInRegion(cuboid, event.getTo())) {
                if (!main.getItemManager().hasMenuItem(player)) continue;
                main.getInventory().returnPlayerInv(player);
            }
        }
    }

    private BlockVector3 getBlockVector3(Location loc) {
        return main.getRegionManager().getBlockVector3(loc);
    }

    private boolean isInRegion(CuboidRegion region, Location location) {
        return region.contains(getBlockVector3(location));
    }
}
