package net.teamuni.shootingtest.config;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import lombok.Getter;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class RegionManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration regionFile = null;
    private final ConfigurationSection section;
    @Getter
    private final Map<String, ProtectedCuboidRegion> region = new HashMap<>();

    public RegionManager(ShootingTest instance) {
        this.main = instance;
        createRegionYml();
        this.section = this.regionFile.getConfigurationSection("region");
        if (section == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[ShootingTest] Doesn't exist the region section in region.yml.");
        }
        registerRegion();
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

    public void createRegion(Player player, String name) {
        Location pos1 = main.getSetRegion().getPositionMap().get("first_position");
        Location pos2 = main.getSetRegion().getPositionMap().get("second_position");
        Set<String> regions = section.getKeys(false);

        if (pos1 == null || pos2 == null) {
            String message = main.getMessageManager().getConfig().getString("region_insufficient_info", "&cInsufficient information to create a region.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        if (regions.contains(name)) {
            String message = main.getMessageManager().getConfig().getString("region_already_exist", "&cThe name of region already exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        section.createSection(name);
        ConfigurationSection section1 = section.getConfigurationSection(name);
        assert section1 != null;
        ConfigurationSection section2 = section1.createSection("first_position");
        ConfigurationSection section3 = section1.createSection("second_position");
        savePosition(section2, pos1);
        savePosition(section3, pos2);
        region.put(name, new ProtectedCuboidRegion(name, getBlockVector3(pos1), getBlockVector3(pos2)));
        main.getSetRegion().getPositionMap().clear();

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
        region.remove(name);

        String message = main.getMessageManager().getConfig().getString("region_removed", "&aRegion has been removed successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void seeRegions(Player player) {
        Set<String> regionKeys = section.getKeys(false);
        if (regionKeys.isEmpty()) {
            String message = main.getMessageManager().getConfig().getString("region_empty", "&cThere are no regions to see.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
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

    public void registerRegion() {
        if (section.getKeys(false).isEmpty()) return;
        for (String regionName : section.getKeys(false)) {
            ConfigurationSection section1 = section.getConfigurationSection(regionName);
            if (section1 == null) continue;
            ConfigurationSection section2 = section1.getConfigurationSection("first_position");
            if (section2 == null) continue;
            ConfigurationSection section3 = section1.getConfigurationSection("second_position");
            if (section3 == null) continue;

            BlockVector3 pos1 = BlockVector3.at(section2.getDouble("x"),
                    section2.getDouble("y"),
                    section2.getDouble("z"));
            BlockVector3 pos2 = BlockVector3.at(section3.getDouble("x"),
                    section3.getDouble("y"),
                    section3.getDouble("z"));
            region.put(regionName, new ProtectedCuboidRegion(regionName, pos1, pos2));
        }
    }

    public BlockVector3 getBlockVector3(Location loc) {
        return BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}
