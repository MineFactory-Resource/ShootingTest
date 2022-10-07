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
        if (sender instanceof Player player) {

            if (command.getName().equalsIgnoreCase("st") && player.hasPermission("st.manage")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (args.length != 1) {
                        sendWarningMsg(player);
                        return false;
                    }
                    main.reloadConfig();
                    main.getMessageManager().reload();
                    main.getItemManager().reload();
                    main.getDummyRespawn().respawnDummies();
                    for (String reloadMessages : main.getMessageManager().getConfig().getStringList("reload_message")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessages));
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (args.length != 1) {
                        sendWarningMsg(player);
                        return false;
                    }
                    for (String cmd : main.getMessageManager().getConfig().getStringList("commands")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', cmd));
                    }
                } else if (args[0].equalsIgnoreCase("region")) {
                    switch (args[1]) {
                        case "create":
                            if (args.length != 3) {
                                sendWarningMsg(player);
                                return false;
                            }
                            main.getRegionManager().createRegion(player, args[2]);
                            break;
                        case "remove":
                            if (args.length != 3) {
                                sendWarningMsg(player);
                                return false;
                            }
                            main.getRegionManager().removeRegion(player, args[2]);
                            break;
                        case "set":
                            if (args.length != 5) {
                                sendWarningMsg(player);
                                return false;
                            }
                            if (args[4].equals("1")) {
                                main.getRegionManager().setFirstPosition(player, args[2]);
                            } else if (args[4].equals("2")) {
                                main.getRegionManager().setSecondPosition(player, args[2]);
                            } else {
                                sendWarningMsg(player);
                                return false;
                            }
                            break;
                        case "see":
                            if (args[2].equalsIgnoreCase("list")) {
                                if (args.length != 3) {
                                    sendWarningMsg(player);
                                    return false;
                                }
                                main.getRegionManager().seeRegions(player);
                            } else if (args[2].equalsIgnoreCase("positions")) {
                                if (args.length != 4) {
                                    sendWarningMsg(player);
                                    return false;
                                }
                                main.getRegionManager().seePositions(player, args[3]);
                            }
                            break;
                        default:
                            sendWarningMsg(player);
                            break;
                    }
                } else if (args[0].equalsIgnoreCase("wand")) {
                    if (args.length != 1) {
                        sendWarningMsg(player);
                        return false;
                    }
                    main.getItemManager().giveWand(player);
                }
            }
        }
        return false;
    }

    public void sendWarningMsg(Player player) {
        String message = main.getMessageManager().getConfig().getString("incorrect_command", "&cThe command cannot be executed!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
