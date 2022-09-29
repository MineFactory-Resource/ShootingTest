package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DummyManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration dummyFile = null;

    public DummyManager(ShootingTest instance) {
        this.main = instance;
        createDummyYml();
    }

    public void createDummyYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "dummy.yml");
        }
        if (!file.exists()) {
            main.saveResource("dummy.yml", false);
        }
        this.dummyFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.dummyFile;
    }

    public void save() {
        if (this.file == null || this.dummyFile == null) return;

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "dummy.yml");
        }

        this.dummyFile = YamlConfiguration.loadConfiguration(file);
    }
}
