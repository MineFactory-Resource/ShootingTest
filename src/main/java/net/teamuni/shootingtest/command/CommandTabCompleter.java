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
                tabCompleteList.add("dummy");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("region")) {
                    tabCompleteList.add("create");
                    tabCompleteList.add("remove");
                    tabCompleteList.add("see");
                } else if (args[0].equalsIgnoreCase("dummy")) {
                    tabCompleteList.add("create");
                    tabCompleteList.add("remove");
                    tabCompleteList.add("list");
                    tabCompleteList.add("tp");
                }
            }
            if (args[0].equalsIgnoreCase("region")) {
                switch (args[1]) {
                    case "create":
                    case "remove":
                        if (args.length == 3) {
                            tabCompleteList.add("{name}");
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
        }
        return null;
    }
}
