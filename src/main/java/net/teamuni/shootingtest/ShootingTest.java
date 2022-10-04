package net.teamuni.shootingtest;

import lombok.Getter;
import net.teamuni.shootingtest.command.CommandTabCompleter;
import net.teamuni.shootingtest.command.DummyCmd;
import net.teamuni.shootingtest.command.ShootingTestCmd;
import net.teamuni.shootingtest.config.DummyManager;
import net.teamuni.shootingtest.config.ItemManager;
import net.teamuni.shootingtest.config.MessageManager;
import net.teamuni.shootingtest.events.DummyDamage;
import net.teamuni.shootingtest.events.DummyRespawn;
import net.teamuni.shootingtest.events.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class ShootingTest extends JavaPlugin {
    private ItemManager itemManager;
    private MessageManager messageManager;
    private DummyManager dummyManager;
    private ShootingTestInv inventory;
    private DummyRespawn dummyRespawn;

    @Override
    public void onEnable() {
        this.messageManager = new MessageManager(this);
        this.itemManager = new ItemManager(this);
        this.dummyManager = new DummyManager(this);
        this.inventory = new ShootingTestInv(this);
        this.dummyRespawn = new DummyRespawn(this);
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerTeleport(this), this);
        Bukkit.getPluginManager().registerEvents(this.inventory, this);
        Bukkit.getPluginManager().registerEvents(this.dummyRespawn, this);
        Bukkit.getPluginManager().registerEvents(new DummyDamage(this), this);
        getCommand("st").setExecutor(new ShootingTestCmd(this));
        getCommand("st").setTabCompleter(new CommandTabCompleter());
        getCommand("dummy").setExecutor(new DummyCmd(this));
        getCommand("dummy").setTabCompleter(new CommandTabCompleter());

        if (!getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens is not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
        }
        if (!getServer().getPluginManager().getPlugin("GunsCore").isEnabled()) {
            getLogger().log(Level.SEVERE, "GunsCore is not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        this.getDummyManager().save();
    }
}
