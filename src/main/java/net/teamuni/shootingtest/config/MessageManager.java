package net.teamuni.shootingtest.config;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageManager {
    private final ShootingTest main = ShootingTest.getInstance();
    private static File file;
    private static FileConfiguration messagesFile;

    public void createMessagesYml() {
        file = new File(main.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            main.saveResource("messages.yml", false);
        }
        messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return messagesFile;
    }

    public static void save() {
        try {
            messagesFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        messagesFile = YamlConfiguration.loadConfiguration(file);
    }
}
