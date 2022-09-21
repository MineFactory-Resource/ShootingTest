package net.teamuni.shootingtest.Command;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShootingTestCmd implements CommandExecutor {
    private final ShootingTest main = ShootingTest.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("사격장") && args[0].equalsIgnoreCase("위치설정")
                    && player.hasPermission("st.manage")) {
                main.getConfig().set("range.world", player.getLocation().getWorld().getName());
                main.getConfig().set("range.x", player.getLocation().getX());
                main.getConfig().set("range.y", player.getLocation().getY());
                main.getConfig().set("range.z", player.getLocation().getZ());
                main.getConfig().set("range.yaw", player.getLocation().getYaw());
                main.getConfig().set("range.pitch", player.getLocation().getPitch());
                main.saveConfig();
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Shooting Range point has been set!");
                return false;
            }
        }
        return false;
    }
}
