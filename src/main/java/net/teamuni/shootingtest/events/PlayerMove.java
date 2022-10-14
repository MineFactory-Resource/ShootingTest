package net.teamuni.shootingtest.events;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

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
        for (ProtectedCuboidRegion cuboid : main.getRegionManager().getRegion().values()) {
            if (cuboid.contains(getBlockVector3(event.getTo()))
                    && !cuboid.contains(getBlockVector3(event.getFrom()))) {
                if (hasMenuItem(player)) continue;
                main.getInventory().setPlayerInv(player);
            } else if (!cuboid.contains(getBlockVector3(event.getTo()))
                    && cuboid.contains(getBlockVector3(event.getFrom()))) {
                if (!hasMenuItem(player)) continue;
                main.getInventory().returnPlayerInv(player);
            }
        }
    }

    public boolean hasMenuItem(Player player) {
        Set<ItemMeta> menuItemMeta = main.getInventory().getStItemMetaSet();
        Set<ItemMeta> invItemMetaSet = new HashSet<>();
        for (int i = 0; i <= 8; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            invItemMetaSet.add(player.getInventory().getItem(i).getItemMeta());
        }
        for (ItemMeta invItemMeta : invItemMetaSet) {
            if (invItemMeta == null) continue;
            if (menuItemMeta.contains(invItemMeta)) return true;
        }
        return false;
    }

    private BlockVector3 getBlockVector3(Location loc) {
        return main.getRegionManager().getBlockVector3(loc);
    }
}
