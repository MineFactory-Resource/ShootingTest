package net.teamuni.shootingtest;

import net.kyori.adventure.text.Component;
import net.teamuni.shootingtest.config.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShootingTestInv implements Listener {

    @NotNull
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private final Map<Integer, ItemStack> stItem = new HashMap<>();
    private final Set<ItemMeta> stItemMetaSet = new HashSet<>();
    private final Inventory inventory;
    private final ShootingTest main = ShootingTest.getInstance();
    private final ItemManager itemManager = new ItemManager();

    public ShootingTestInv() {
        this.stItem.putAll(itemManager.getItems("Items"));
        for (ItemStack itemStack : stItem.values()) {
            this.stItemMetaSet.add(itemStack.getItemMeta());
        }
        this.inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Guns"));
        initializeItems();
    }

    public void setPlayerInv(Player player) {
        ItemStack[] inv = player.getInventory().getContents();
        playerInventory.put(player.getUniqueId(), inv);
        player.getInventory().clear();

        for (Map.Entry<Integer, ItemStack> items : stItem.entrySet()) {
            player.getInventory().setItem(items.getKey(), items.getValue());
        }
    }

    public void returnPlayerInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(playerInventory.get(player.getUniqueId()));
        playerInventory.remove(player.getUniqueId());
    }

    public void initializeItems() {
        inventory.addItem(itemManager.createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "Example Lore", "Example Lore2"));
        inventory.addItem(itemManager.createGuiItem(Material.IRON_HELMET, "Example Helmet", "Example Lore", "Example Lore2"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        List<String> worlds = main.getConfig().getStringList("enable_world");

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!worlds.contains(player.getWorld().getName())) return;
            if (!stItemMetaSet.contains(player.getInventory().getItemInMainHand().getItemMeta())) return;
            player.openInventory(inventory);
        }
    }
}
