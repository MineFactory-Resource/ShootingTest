package net.teamuni.shootingtest.inventory;

import lombok.Getter;
import net.teamuni.shootingtest.ShootingTest;
import net.teamuni.shootingtest.inventory.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {
    private final ShootingTest main;
    @NotNull
    @Getter
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();

    public InventoryManager(ShootingTest instance) {
        this.main = instance;
    }

    public void setPlayerInv(Player player) {
        ItemStack[] inv = player.getInventory().getContents();
        this.playerInventory.put(player.getUniqueId(), inv);
        player.getInventory().clear();

        for (Map.Entry<Integer, InventoryItem> items : main.getItemManager().getInventoryItems().entrySet()) {
            player.getInventory().setItem(items.getKey(), items.getValue().itemStack());
        }
    }

    public void returnPlayerInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(this.playerInventory.get(player.getUniqueId()));
        this.playerInventory.remove(player.getUniqueId());
    }
}
