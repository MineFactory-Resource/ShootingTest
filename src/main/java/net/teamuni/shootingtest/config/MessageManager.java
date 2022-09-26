package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration messagesFile = null;

    public MessageManager(ShootingTest instance) {
        this.main = instance;
        createMessagesYml();
    }

    public void createMessagesYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "messages.yml");
        }
        if (!file.exists()) {
            main.saveResource("messages.yml", false);
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.messagesFile;
    }

    public void save() {
        if (this.file == null || this.messagesFile == null) return;

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "messages.yml");
        }

        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }
}
