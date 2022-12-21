package net.teamuni.shootingtest;

import lombok.Getter;
import net.teamuni.shootingtest.command.CommandTabCompleter;
import net.teamuni.shootingtest.command.ShootingTestCmd;
import net.teamuni.shootingtest.config.stInventory;
import net.teamuni.shootingtest.dummy.DummyDamage;
import net.teamuni.shootingtest.dummy.DummyManager;
import net.teamuni.shootingtest.config.ItemManager;
import net.teamuni.shootingtest.config.MessageManager;
import net.teamuni.shootingtest.dummy.DummyRespawn;
import net.teamuni.shootingtest.region.RegionManager;
import net.teamuni.shootingtest.events.*;
import net.teamuni.shootingtest.region.SetRegion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class ShootingTest extends JavaPlugin {
    private ItemManager itemManager;
    private MessageManager messageManager;
    private DummyManager dummyManager;
    private RegionManager regionManager;
    private stInventory stInventory;
    private DummyRespawn dummyRespawn;
    private SetRegion setRegion;

    @Override
    public void onEnable() {
        this.regionManager = new RegionManager(this);
        this.dummyManager = new DummyManager(this);
        this.dummyRespawn = new DummyRespawn(this);
        this.itemManager = new ItemManager(this);
        this.stInventory = new stInventory(this);
        this.setRegion = new SetRegion(this);
        this.messageManager = new MessageManager(this);
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this.dummyRespawn, this);
        Bukkit.getPluginManager().registerEvents(this.stInventory, this);
        Bukkit.getPluginManager().registerEvents(this.setRegion, this);
        Bukkit.getPluginManager().registerEvents(new DummyDamage(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleport(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(this), this);
        getCommand("사격장").setExecutor(new ShootingTestCmd(this));
        getCommand("사격장").setTabCompleter(new CommandTabCompleter());

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
        this.getRegionManager().save();
    }
}
