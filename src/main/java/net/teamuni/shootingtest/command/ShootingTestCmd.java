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

            if (command.getName().equalsIgnoreCase("사격장") && player.hasPermission("st.manage")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "리로드" -> {
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
                        }
                        case "도움말" -> {
                            if (args.length != 1) {
                                sendWarningMsg(player);
                                return false;
                            }
                            for (String cmd : main.getMessageManager().getConfig().getStringList("commands")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cmd));
                            }
                        }
                        case "지역" -> {
                            switch (args[1]) {
                                case "생성" -> {
                                    if (args.length != 3) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getRegionManager().createRegion(player, args[2]);
                                }
                                case "제거" -> {
                                    if (args.length != 3) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getRegionManager().removeRegion(player, args[2]);
                                }
                                case "확인" -> {
                                    if (args[2].equalsIgnoreCase("목록")) {
                                        if (args.length != 3) {
                                            sendWarningMsg(player);
                                            return false;
                                        }
                                        main.getRegionManager().seeRegions(player);
                                    } else if (args[2].equalsIgnoreCase("위치")) {
                                        if (args.length != 4) {
                                            sendWarningMsg(player);
                                            return false;
                                        }
                                        main.getRegionManager().seePositions(player, args[3]);
                                    }
                                }
                                default -> sendWarningMsg(player);
                            }
                        }
                        case "완드" -> {
                            if (args.length != 1) {
                                sendWarningMsg(player);
                                return false;
                            }
                            main.getItemManager().giveWand(player);
                        }
                        case "타겟" -> {
                            switch (args[1]) {
                                case "생성" -> {
                                    if (args.length != 3) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getDummyManager().createDummy(player, args[2], player.getLocation());
                                }
                                case "제거" -> {
                                    if (args.length != 3) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getDummyManager().removeDummy(player, args[2]);
                                }
                                case "목록" -> {
                                    if (args.length != 2) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getDummyManager().seeDummies(player);
                                }
                                case "텔레포트" -> {
                                    if (args.length != 3) {
                                        sendWarningMsg(player);
                                        return false;
                                    }
                                    main.getDummyManager().teleportToDummy(player, args[2]);
                                }
                                default -> sendWarningMsg(player);
                            }
                        }
                    }
                } else {
                    for (String cmd : main.getMessageManager().getConfig().getStringList("commands")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', cmd));
                    }
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
