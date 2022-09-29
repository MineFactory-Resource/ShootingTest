package net.teamuni.shootingtest.command;

import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DummyCmd implements CommandExecutor {
    private final ShootingTest main;
    public DummyCmd(ShootingTest instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("dummy") && player.hasPermission("st.manage")) {
                if (args[0].equalsIgnoreCase("create")) {
                    main.getDummy().createDummy(player, args[1], player.getLocation());
                    return false;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    main.getDummy().removeDummy(player, args[1]);
                    return false;
                }
            }
            return false;
        }
        return true;
    }
}
