package net.teamuni.shootingtest;

import net.teamuni.shootingtest.config.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShootingTestInv {

    @NotNull
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private final Map<Integer, ItemStack> stItem = new HashMap<>();

    public ShootingTestInv() {
        ShootingTest main = ShootingTest.getInstance();
        ItemManager itemManager = main.itemManager;
        this.stItem.putAll(itemManager.getItems("Items"));
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
}
