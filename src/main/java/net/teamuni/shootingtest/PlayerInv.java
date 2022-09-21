package net.teamuni.shootingtest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInv {
    public static PlayerInv getPlayerInv() {
        return new PlayerInv();
    }

    @NotNull
    private final Map<UUID, ItemStack[]> playerInventory = new HashMap<>();

    public void setPlayerInv(Player player) {
        ItemStack[] inv = player.getInventory().getContents();

        playerInventory.put(player.getUniqueId(), inv);
        player.getInventory().clear();
    }

    public void returnPlayerInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(playerInventory.get(player.getUniqueId()));
        playerInventory.remove(player.getUniqueId());
    }
}
