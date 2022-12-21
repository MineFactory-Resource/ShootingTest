package net.teamuni.shootingtest.config;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.teamuni.gunscore.api.GunsAPI;
import net.teamuni.gunscore.gunslib.object.Gun;
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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class stInventory implements Listener {
    private final ShootingTest main;
    @NotNull
    @Getter
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private final Map<Integer, stInventoryItem> stItem;
    private final Map<Integer, ItemStack> gun;
    private final Inventory inventory;

    public stInventory(ShootingTest instance) {
        this.main = instance;
        this.stItem = instance.getItemManager().getInventoryItems();
        this.gun = instance.getItemManager().getGunItem("Guns");
        this.inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Guns"));
        initializeItems();
    }

    public void setPlayerInv(Player player) {
        ItemStack[] inv = player.getInventory().getContents();
        this.playerInventory.put(player.getUniqueId(), inv);
        player.getInventory().clear();

        for (Map.Entry<Integer, stInventoryItem> items : stItem.entrySet()) {
            player.getInventory().setItem(items.getKey(), items.getValue().itemStack());
        }
    }

    public void returnPlayerInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(this.playerInventory.get(player.getUniqueId()));
        this.playerInventory.remove(player.getUniqueId());
    }

    public void initializeItems() {
        for (Map.Entry<Integer, ItemStack> guns : gun.entrySet()) {
            this.inventory.setItem(guns.getKey(), guns.getValue());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(this.inventory)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = main.getConfig().getInt("slot_where_gun_will_place");
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        Gun gun = GunsAPI.getGun(item);
        if (gun == null) return;

        player.getInventory().setItem(slot, gun.getItem(player).clone());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getInventory().equals(this.inventory)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        for (stInventoryItem inventoryItem : this.stItem.values()) {
            if (!inventoryItem.itemStack().equals(itemStack)) continue;
            switch (inventoryItem.type()) {
                case MENU -> {
                    player.openInventory(this.inventory);
                }
                case SPAWN -> {
                    player.performCommand(main.getConfig().getString("spawn_command", "spawn"));
                }
            }
        }
    }
}
