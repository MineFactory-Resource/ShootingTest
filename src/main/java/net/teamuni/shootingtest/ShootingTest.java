package net.teamuni.shootingtest;

import lombok.Getter;
import net.teamuni.shootingtest.command.CommandTabCompleter;
import net.teamuni.shootingtest.command.ShootingTestCmd;
import net.teamuni.shootingtest.config.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ShootingTest extends JavaPlugin {

    private static ShootingTest shootingTest;
    private static MessageManager messages;
    public static ShootingTest getInstance() {
        return shootingTest;
    }

    @Override
    public void onEnable() {
        shootingTest = this;
        messages = new MessageManager();
        messages.createMessagesYml();
        saveDefaultConfig();
        getCommand("사격장").setExecutor(new ShootingTestCmd());
        getCommand("사격장").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
