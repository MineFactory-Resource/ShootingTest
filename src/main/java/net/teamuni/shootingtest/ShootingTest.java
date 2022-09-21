package net.teamuni.shootingtest;

import net.teamuni.shootingtest.Command.CommandTabCompleter;
import net.teamuni.shootingtest.Command.ShootingTestCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShootingTest extends JavaPlugin {

    private static ShootingTest shootingTest;
    public static ShootingTest getInstance() {
        return shootingTest;
    }

    @Override
    public void onEnable() {
        shootingTest = this;
        saveDefaultConfig();
        getCommand("사격장").setExecutor(new ShootingTestCmd());
        getCommand("사격장").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
