package net.teamuni.shootingtest.command;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShootingTestCmd implements CommandExecutor {
    private final ShootingTest main;
    public ShootingTestCmd(ShootingTest instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("st") && args[0].equalsIgnoreCase("reload")
                    && player.hasPermission("st.manage")) {
                main.reloadConfig();
                main.getMessageManager().reload();
                main.getItemManager().reload();
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Shooting Range point has been set!");
            }
        }
        return false;
    }
}
