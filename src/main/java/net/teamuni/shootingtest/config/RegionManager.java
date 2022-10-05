package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RegionManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration regionFile = null;
    public RegionManager(ShootingTest instance) {
        this.main = instance;
        createRegionYml();
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
}
