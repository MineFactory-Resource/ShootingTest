package net.teamuni.shootingtest.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        for (CuboidRegion cuboid : main.getRegionManager().getRegion().values()) {
            if (isInRegion(cuboid, event.getTo()) && !isInRegion(cuboid, event.getFrom())) {
                if (main.getItemManager().hasMenuItem(player)) continue;
                main.getStInventory().setPlayerInv(player);
            } else if (isInRegion(cuboid, event.getFrom()) && !isInRegion(cuboid, event.getTo())) {
                if (!main.getItemManager().hasMenuItem(player)) continue;
                main.getStInventory().returnPlayerInv(player);
            }
        }
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
        if (main.getSetRegion().getPositionMap().isEmpty()) return;
        main.getSetRegion().getPositionMap().clear();
    }

    private BlockVector3 getBlockVector3(Location loc) {
        return main.getRegionManager().getBlockVector3(loc);
    }

    private boolean isInRegion(CuboidRegion region, Location location) {
        if (!isSameWorld(region, location.getWorld())) {
            return false;
        }
        return region.contains(getBlockVector3(location));
    }

    private boolean isSameWorld(CuboidRegion region, World world) {
        return region.getWorld().equals(BukkitAdapter.adapt(world));
    }
}
