package net.teamuni.shootingtest.events;

import lombok.Getter;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class SetRegion implements Listener {
    private final ShootingTest main;
    @Getter
    private final Map<String, Location> positionMap = new HashMap<>();
    public SetRegion(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        String locationInfo = block.getX() + ", " + block.getY() + ", " + block.getZ();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && hasWandOnHand(player)) {
            positionMap.put("first_position", block.getLocation());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "First position is set to the position (" + locationInfo + ").");
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && hasWandOnHand(player)) {
            positionMap.put("second_position", block.getLocation());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Second position is set to the position (" + locationInfo + ").");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!hasWandOnHand(player)) return;
        event.setCancelled(true);
    }

    private boolean hasWandOnHand(Player player) {
        return player.getInventory().getItemInMainHand().equals(main.getItemManager().getRegionWand());
    }
}
