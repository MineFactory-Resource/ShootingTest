package net.teamuni.shootingtest.config;

import net.kyori.adventure.text.Component;
import net.teamuni.gunscore.api.GunsAPI;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public FileConfiguration getConfig() {
        return this.itemsFile;
    }

    public void save() {
        if (this.file == null || this.itemsFile == null) return;

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "items.yml");
        }

        this.itemsFile = YamlConfiguration.loadConfiguration(file);
    }

    @NotNull
    public Map<Integer, ItemStack> getItems(String path) {
        Map<Integer, ItemStack> items = new HashMap<>();
        Set<String> itemKeys = this.getConfig().getConfigurationSection(path).getKeys(false);
        if (itemKeys.isEmpty()) {
            throw new IllegalArgumentException("items.yml에서 정보를 가져오는 도중 문제가 발생했습니다.");
        }
        for (String key : itemKeys) {
            int slot = this.getConfig().getInt(path + "." + key + ".slot");
            try {
                ItemStack item = new ItemStack(Material.valueOf(this.getConfig().getString(path + "." + key + ".item_type")));
                ItemMeta meta = item.getItemMeta();
                String itemName = this.getConfig().getString(path + "." + key + ".name");
                List<Component> loreList = new ArrayList<>();

                for (String lores : this.getConfig().getStringList(path + "." + key + ".lore")) {
                    loreList.add(Component.text(ChatColor.translateAlternateColorCodes('&', lores)));
                }
                meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', itemName)));
                meta.lore(loreList);
                item.setItemMeta(meta);
                items.put(slot, item);
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public Map<Integer, ItemStack> getGunItem(String path) {
        Map<Integer, ItemStack> guns = new HashMap<>();
        Set<String> gunKeys = this.getConfig().getConfigurationSection(path).getKeys(false);
        if (gunKeys.isEmpty()) {
            throw new IllegalArgumentException("items.yml에서 정보를 가져오는 도중 문제가 발생했습니다.");
        }
        for (String key : gunKeys) {
            int slot = this.getConfig().getInt(path + "." + key + ".slot");
            try {
                ItemStack gun = GunsAPI.getGun(key).getItem();
                ItemMeta gunMeta = gun.getItemMeta();
                String gunName = this.getConfig().getString(path + "." + key + ".name");
                List<Component> loreList = new ArrayList<>();

                for (String lores : this.getConfig().getStringList(path + "." + key + ".lore")) {
                    loreList.add(Component.text(ChatColor.translateAlternateColorCodes('&', lores)));
                }
                gunMeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', gunName)));
                gunMeta.lore(loreList);
                gun.setItemMeta(gunMeta);
                gun.setAmount(1);
                guns.put(slot, gun);
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return guns;
    }
}
