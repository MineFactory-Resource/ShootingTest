package net.teamuni.shootingtest.inventory;

import net.teamuni.shootingtest.config.ItemType;
import org.bukkit.inventory.ItemStack;

public record InventoryItem(ItemStack itemStack, ItemType type) {
}
