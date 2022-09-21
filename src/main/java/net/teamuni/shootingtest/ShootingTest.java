package net.teamuni.shootingtest;

import lombok.Getter;
import net.teamuni.shootingtest.command.CommandTabCompleter;
import net.teamuni.shootingtest.command.ShootingTestCmd;
import net.teamuni.shootingtest.config.MessageManager;
import net.teamuni.shootingtest.events.PlayerTeleport;
import org.bukkit.Bukkit;
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
        Bukkit.getPluginManager().registerEvents(new PlayerTeleport(), this);
        getCommand("사격장").setExecutor(new ShootingTestCmd());
        getCommand("사격장").setTabCompleter(new CommandTabCompleter());
        getCommand("st").setExecutor(new ShootingTestCmd());
        getCommand("st").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
