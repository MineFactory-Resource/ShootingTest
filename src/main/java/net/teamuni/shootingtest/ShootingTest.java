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
    private ItemManager itemManager;
    private MessageManager messageManager;
    private ShootingTestInv inventory;

    public ItemManager getItemManager() {
        return itemManager;
    }
    public MessageManager getMessageManager() {
        return messageManager;
    }
    public ShootingTestInv getInventory() {
        return inventory;
    }

    @Override
    public void onEnable() {
        this.messageManager = new MessageManager(this);
        this.itemManager = new ItemManager(this);
        this.inventory = new ShootingTestInv(this);
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerTeleport(this), this);
        getCommand("st").setExecutor(new ShootingTestCmd(this));
        getCommand("st").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
