package net.teamuni.shootingtest.inventory;

import net.kyori.adventure.text.Component;
import net.teamuni.gunscore.api.GunsAPI;
import net.teamuni.gunscore.api.weapons.Gun;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GunSelectionGUI implements Listener {
    private final ShootingTest main;
    private final Map<UUID, Inventory> inventoryMap = new HashMap<>();

    public GunSelectionGUI(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if (!event.getInventory().equals(this.inventoryMap.get(uuid))) return;
        event.setCancelled(true);

        int slot = main.getConfig().getInt("slot_where_gun_will_place");
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        Gun gun = GunsAPI.getGun(item);
        if (gun == null) return;

        player.getInventory().setItem(slot, gun.getItem(player));
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getInventory().equals(this.inventoryMap.get(player.getUniqueId()))) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        for (InventoryItem inventoryItem : main.getItemManager().getInventoryItems().values()) {
            if (!inventoryItem.itemStack().equals(itemStack)) continue;
            switch (inventoryItem.type()) {
                case MENU -> player.openInventory(gunInventory(player));
                case SPAWN -> player.performCommand(main.getConfig().getString("spawn_command", "spawn"));
            }
        }
    }

    public Inventory gunInventory(Player player) {
        UUID uuid = player.getUniqueId();
        Inventory inventory;

        if (!this.inventoryMap.containsKey(uuid)) {
            inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Guns"));
        } else {
            inventory = this.inventoryMap.get(uuid);
        }

        for (Map.Entry<Integer, ItemStack> entry : main.getItemManager().getGunItem("Guns").entrySet()) {
            Gun gun = GunsAPI.getGun(entry.getValue());
            if (gun == null) continue;
            ItemStack item = gun.getItem(player);
            item.setAmount(1);
            inventory.setItem(entry.getKey(), item);
        }
        this.inventoryMap.put(uuid, inventory);

        return inventory;
    }
}
