package net.teamuni.shootingtest.config;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.teamuni.gunscore.api.GunsAPI;
import net.teamuni.shootingtest.ShootingTest;
import net.teamuni.shootingtest.config.ItemManager;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShootingTestInv implements Listener {

    @NotNull
    @Getter
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private final Map<Integer, ItemStack> stItem = new HashMap<>();
    private final Map<Integer, ItemStack> gun = new HashMap<>();
    @Getter
    private final Set<ItemMeta> stItemMetaSet = new HashSet<>();
    private final Inventory inventory;

    public ShootingTestInv(ShootingTest instance) {
        ItemManager itemManager = instance.getItemManager();
        this.stItem.putAll(itemManager.getItems("InventoryItems"));
        this.gun.putAll(itemManager.getGunItem("Guns"));
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
        for (Map.Entry<Integer, ItemStack> guns : gun.entrySet()) {
            inventory.setItem(guns.getKey(), guns.getValue());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        if (event.getCurrentItem() == null || GunsAPI.getGun(event.getCurrentItem()) == null) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        player.getInventory().setItem(0, GunsAPI.getGun(event.getCurrentItem()).getItem(player).clone());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!stItemMetaSet.contains(player.getInventory().getItemInMainHand().getItemMeta())) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            player.openInventory(inventory);
        }
    }

    public enum ItemType {
        SPAWN, MENU, UNKNOWN
    }
}