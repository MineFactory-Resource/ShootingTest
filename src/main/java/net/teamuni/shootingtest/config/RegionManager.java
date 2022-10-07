package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

public class RegionManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration regionFile = null;
    private final ConfigurationSection section;
    public RegionManager(ShootingTest instance) {
        this.main = instance;
        createRegionYml();
        this.section = this.regionFile.getConfigurationSection("region");
        if (section == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[ShootingTest] Doesn't exist the region section in region.yml.");
        }
    }

    public void createRegionYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "region.yml");
        }
        if (!file.exists()) {
            main.saveResource("region.yml", false);
        }
        this.regionFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.regionFile;
    }

    public void save() {
        if (this.file == null || this.regionFile == null) return;

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "region.yml");
        }

        this.regionFile = YamlConfiguration.loadConfiguration(file);
    }

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public void createRegion(Player player, String name) {
        Set<String> regions = section.getKeys(false);
        if (regions.contains(name)) {
            String message = main.getMessageManager().getConfig().getString("region_already_exist", "&cThe name of region already exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        section.createSection(name);

        String message = main.getMessageManager().getConfig().getString("region_created", "&aRegion has been created successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void removeRegion(Player player, String name) {
        ConfigurationSection section1 = section.getConfigurationSection(name);
        if (section1 == null) {
            String message = main.getMessageManager().getConfig().getString("region_not_exist", "&cA region try to remove does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        section.set(name, null);

        String message = main.getMessageManager().getConfig().getString("region_removed", "&aRegion has been removed successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void setFirstPosition(Player player, String name) {
        ConfigurationSection section1 = section.getConfigurationSection(name);
        if (section1 == null) {
            String message = main.getMessageManager().getConfig().getString("region_not_exist", "&cA region try to set position does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        ConfigurationSection section2 = section1.createSection("first_position");
        Location blockLoc = this.getTargetBlock(player, 20).getLocation();
        String locationInfo = blockLoc.getX() + ", " + blockLoc.getBlockY() + ", " + blockLoc.getBlockZ();
        this.savePosition(section2, blockLoc);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "First position is set to the position (" + locationInfo + ") of the block you are looking at.");
    }

    public void setSecondPosition(Player player, String name) {
        ConfigurationSection section1 = section.getConfigurationSection(name);
        if (section1 == null) {
            String message = main.getMessageManager().getConfig().getString("region_not_exist", "&cA region try to set position does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        ConfigurationSection section2 = section1.createSection("second_position");
        Location blockLoc = this.getTargetBlock(player, 20).getLocation();
        String locationInfo = blockLoc.getX() + ", " + blockLoc.getY() + ", " + blockLoc.getZ();
        this.savePosition(section2, blockLoc);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Second position is set to the position (" + locationInfo + ") of the block you are looking at.");
    }

    public void seeRegions(Player player) {
        Set<String> regionKeys = section.getKeys(false);
        if (regionKeys.isEmpty()) {
            String message = main.getMessageManager().getConfig().getString("region_empty", "&cThere are no regions to see.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        player.sendMessage(ChatColor.YELLOW + "----------- " + ChatColor.WHITE + "List of region" + ChatColor.YELLOW + " -----------");
        int number = 0;
        for (String keys : regionKeys) {
            ++number;
            player.sendMessage(ChatColor.GOLD + String.valueOf(number) + ". " + keys);
        }
    }

    public void seePositions(Player player, String name) {
        ConfigurationSection section2 = section.getConfigurationSection(name);
        if (section2 == null) {
            String message = main.getMessageManager().getConfig().getString("region_not_exist", "&cA region try to see positions does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "----------- " + ChatColor.WHITE + "Position of " + name + ChatColor.YELLOW + " -----------");

        for (String key : section2.getKeys(false)) {
            ConfigurationSection section3 = section2.getConfigurationSection(key);
            if (section3 == null) continue;

            player.sendMessage(ChatColor.GOLD + key + ": ");
            for (String key2 : section3.getKeys(false)) {
                player.sendMessage("   " + ChatColor.GOLD + key2 + ": " + ChatColor.WHITE + section3.getString(key2));
            }
        }
    }

    public void savePosition(ConfigurationSection section, Location location) {
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
    }
}
