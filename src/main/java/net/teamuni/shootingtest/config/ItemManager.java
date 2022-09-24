package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemManager {

    private final ShootingTest main;
    private File file = null;
    private FileConfiguration itemsFile = null;

    public ItemManager(ShootingTest plugin) {
        this.main = plugin;
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
            ItemStack item = new ItemStack(Material.valueOf(this.getConfig().getString(path + "." + key + ".item_type")));
            items.put(slot, item);
        }
        return items;
    }
}
