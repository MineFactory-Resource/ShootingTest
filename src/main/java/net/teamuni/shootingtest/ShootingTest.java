package net.teamuni.shootingtest;

import lombok.Getter;
import net.teamuni.shootingtest.command.CommandTabCompleter;
import net.teamuni.shootingtest.command.ShootingTestCmd;
import net.teamuni.shootingtest.config.ItemManager;
import net.teamuni.shootingtest.config.MessageManager;
import net.teamuni.shootingtest.events.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ShootingTest extends JavaPlugin {

    private static ShootingTest shootingTest;
    public static ShootingTest getInstance() {
        return shootingTest;
    }
    public ItemManager itemManager;
    public MessageManager messageManager;

    @Override
    public void onEnable() {
        shootingTest = this;
        messageManager = new MessageManager(this);
        itemManager = new ItemManager(this);
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerTeleport(), this);
        getCommand("st").setExecutor(new ShootingTestCmd());
        getCommand("st").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
