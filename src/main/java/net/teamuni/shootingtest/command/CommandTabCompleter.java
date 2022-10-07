package net.teamuni.shootingtest.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("st")) {
            List<String> tabCompleteList = new ArrayList<>();

            if (args.length == 1) {
                tabCompleteList.add("reload");
                tabCompleteList.add("region");
                tabCompleteList.add("help");
                tabCompleteList.add("wand");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("region")) {
                tabCompleteList.add("create");
                tabCompleteList.add("remove");
                tabCompleteList.add("set");
                tabCompleteList.add("see");
            } else {
                switch (args[1]) {
                    case "create":
                    case "remove":
                        if (args.length == 3) {
                            tabCompleteList.add("{name}");
                        }
                        break;
                    case "set":
                        switch (args.length) {
                            case 3 -> tabCompleteList.add("{name}");
                            case 4 -> tabCompleteList.add("position");
                            case 5 -> {
                                tabCompleteList.add("1");
                                tabCompleteList.add("2");
                            }
                        }
                        break;
                    case "see":
                        if (args.length == 3) {
                            tabCompleteList.add("list");
                            tabCompleteList.add("positions");
                        }
                        if (args.length == 4 && args[3].equalsIgnoreCase("positions")) {
                            tabCompleteList.add("{name}");
                        }
                        break;
                    default:
                        break;
                }
            }
            return tabCompleteList;
        } else if (cmd.getName().equalsIgnoreCase("dummy")) {
            List<String> tabCompleteList = new ArrayList<>();
            if (args.length == 1) {
                tabCompleteList.add("create");
                tabCompleteList.add("remove");
            }
            return tabCompleteList;
        }
        return null;
    }
}
