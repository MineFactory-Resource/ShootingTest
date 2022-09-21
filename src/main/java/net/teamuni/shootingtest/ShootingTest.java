package net.teamuni.shootingtest;

import org.bukkit.plugin.java.JavaPlugin;

public final class ShootingTest extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
