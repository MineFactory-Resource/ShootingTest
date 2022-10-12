package net.teamuni.shootingtest.events;

import net.teamuni.shootingtest.ShootingTest;
import net.teamuni.shootingtest.region.Cuboid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
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
        for (Cuboid cuboid : main.getRegionManager().getRegion().values()) {
            if (cuboid.contains(event.getTo())
                    && !cuboid.contains(event.getFrom())) {
                if (hasMenuItem(player)) continue;
                main.getInventory().setPlayerInv(player);
            } else if (!cuboid.contains(event.getTo())
                    && cuboid.contains(event.getFrom())) {
                if (!hasMenuItem(player)) continue;
                main.getInventory().returnPlayerInv(player);
            }
        }
    }

    public boolean hasMenuItem(Player player) {
        Set<ItemMeta> items = main.getInventory().getStItemMetaSet();
        List<ItemMeta> itemList = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            itemList.add(player.getInventory().getItem(i).getItemMeta());
        }
        for (ItemMeta item : itemList) {
            if (item == null) continue;
            if (items.contains(item)) return true;
        }
        itemList.clear();
        return false;
    }
}
