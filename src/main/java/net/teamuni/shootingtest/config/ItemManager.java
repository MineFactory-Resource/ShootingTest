package net.teamuni.shootingtest.config;

import net.kyori.adventure.text.Component;
import net.teamuni.gunscore.api.GunsAPI;
import net.teamuni.gunscore.gunslib.object.Gun;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemManager {

    private final ShootingTest main;
    private File file = null;
    private FileConfiguration itemsFile = null;

    public ItemManager(ShootingTest instance) {
        this.main = instance;
        createItemsYml();
    }

    public void createItemsYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "items.yml");
        }
        if (!file.exists()) {
            main.saveResource("items.yml", false);
        }
        this.itemsFile = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "items.yml");
        }

        this.itemsFile = YamlConfiguration.loadConfiguration(file);
    }

    public Map<Integer, stInventoryItem> getInventoryItems() {
        ConfigurationSection section = this.itemsFile.getConfigurationSection("InventoryItems");
        if (section == null) return null;

        Set<String> itemKeys = section.getKeys(false);
        if (itemKeys.isEmpty()) {
            throw new IllegalArgumentException("items.yml에서 정보를 가져오는 도중 문제가 발생했습니다.");
        }

        Map<Integer, stInventoryItem> items = new HashMap<>();

        for (String key : itemKeys) {
            ConfigurationSection section2 = section.getConfigurationSection(key);
            if (section2 == null) continue;
            int slot = section2.getInt("slot");
            try {
                ItemStack item = new ItemStack(Material.valueOf(section2.getString("item_type")));
                ItemMeta meta = item.getItemMeta();
                String itemName = section2.getString("name", "");
                ItemType type = ItemType.valueOf(section2.getString("type"));
                List<Component> loreList = new ArrayList<>();

                for (String lores : section2.getStringList("lore")) {
                    loreList.add(Component.text(ChatColor.translateAlternateColorCodes('&', lores)));
                }
                meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', itemName)));
                meta.lore(loreList);
                item.setItemMeta(meta);
                items.put(slot, new stInventoryItem(item, type));
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public Map<Integer, ItemStack> getGunItem(String path) {
        ConfigurationSection section = this.itemsFile.getConfigurationSection(path);
        if (section == null) return null;

        Set<String> gunKeys = section.getKeys(false);
        if (gunKeys.isEmpty()) {
            throw new IllegalArgumentException("items.yml에서 정보를 가져오는 도중 문제가 발생했습니다.");
        }

        Map<Integer, ItemStack> guns = new HashMap<>();

        for (String key : gunKeys) {
            ConfigurationSection section2 = section.getConfigurationSection(key);
            if (section2 == null) continue;
            int slot = section2.getInt("slot");
            try {
                Gun gun = GunsAPI.getGun(key);
                if (gun == null) continue;
                ItemStack gunItem = gun.getItem();
                ItemMeta gunMeta = gunItem.getItemMeta();
                String gunName = section2.getString("name", "");
                List<Component> loreList = new ArrayList<>();

                for (String lores : section2.getStringList("lore")) {
                    loreList.add(Component.text(ChatColor.translateAlternateColorCodes('&', lores)));
                }
                gunMeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', gunName)));
                gunMeta.lore(loreList);
                gunItem.setItemMeta(gunMeta);
                gunItem.setAmount(1);
                guns.put(slot, gunItem);
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return guns;
    }

    public void giveWand(Player player) {
        player.getInventory().addItem(getRegionWand());
    }

    public ItemStack getRegionWand() {
        ConfigurationSection section = this.itemsFile.getConfigurationSection("Region_wand_item");
        if (section == null) return null;
        Material material = Material.valueOf(section.getString("item_type"));
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        String name = section.getString("name", "wand");
        List<Component> loreList = new ArrayList<>();

        for (String lores : section.getStringList("lore")) {
            loreList.add(Component.text(ChatColor.translateAlternateColorCodes('&', lores)));
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        meta.lore(loreList);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public boolean hasMenuItem(Player player) {
        Set<ItemMeta> menuItemMeta = new HashSet<>();
        Set<ItemMeta> invItemMetaSet = new HashSet<>();
        getInventoryItems().forEach((key, value) -> menuItemMeta.add(value.itemStack().getItemMeta()));

        for (int i = 0; i <= 8; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;
            invItemMetaSet.add(item.getItemMeta());
        }
        for (ItemMeta invItemMeta : invItemMetaSet) {
            if (invItemMeta == null) continue;
            if (menuItemMeta.contains(invItemMeta)) return true;
        }
        return false;
    }
}
