package net.teamuni.shootingtest.config;

import org.bukkit.inventory.ItemStack;

public record StInventoryItem(ItemStack itemStack, ItemType type) {
}
