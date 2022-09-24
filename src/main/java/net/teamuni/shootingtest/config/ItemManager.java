package net.teamuni.shootingtest.config;

import net.kyori.adventure.text.Component;
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

    private final ShootingTest main = ShootingTest.getInstance();
    private File file;
    private FileConfiguration itemsFile;

    public void createItemsYml() {
        file = new File(main.getDataFolder(), "items.yml");

        if (!file.exists()) {
            main.saveResource("items.yml", false);
        }
        itemsFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return itemsFile;
    }

    public void save() {
        try {
            itemsFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        itemsFile = YamlConfiguration.loadConfiguration(file);
    }

    @NotNull
    public Map<Integer, ItemStack> getItems(String path) {
        Map<Integer, ItemStack> items = new HashMap<>();
        Set<String> itemKeys = this.get().getConfigurationSection(path).getKeys(false);
        if (itemKeys.isEmpty()) {
            throw new IllegalArgumentException("items.yml에서 정보를 가져오는 도중 문제가 발생했습니다.");
        }
        for (String key : itemKeys) {
            int slot = this.get().getInt(path + "." + key + ".slot");
            try {
                ItemStack item = new ItemStack(Material.valueOf(this.get().getString(path + "." + key + ".item_type")));
                ItemMeta meta = item.getItemMeta();
                String itemName = this.get().getString(path + "." + key + ".name");
                List<Component> loreList = new ArrayList<>();

                for (String lores : this.get().getStringList(path + "." + key + ".lore")) {
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

    public ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        meta.lore(Collections.singletonList(Component.text(ChatColor.translateAlternateColorCodes('&', Arrays.toString(lore)))));

        item.setItemMeta(meta);

        return item;
    }
}
