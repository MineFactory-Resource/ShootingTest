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
        if (cmd.getName().equalsIgnoreCase("사격장")) {
            List<String> tabCompleteList = new ArrayList<>();
            if (args.length > 0) {
                if (args.length == 1) {
                    tabCompleteList.add("reload");
                    tabCompleteList.add("region");
                    tabCompleteList.add("help");
                    tabCompleteList.add("wand");
                    tabCompleteList.add("dummy");
                }
                if (args[0].equalsIgnoreCase("region")) {
                    if (args.length > 1) {
                        if (args.length == 2) {
                            tabCompleteList.add("create");
                            tabCompleteList.add("remove");
                            tabCompleteList.add("see");
                        } else if (args.length == 3 && args[1].equals("see")) {
                            tabCompleteList.add("list");
                            tabCompleteList.add("positions");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("dummy")) {
                    if (args.length == 2) {
                        tabCompleteList.add("create");
                        tabCompleteList.add("remove");
                        tabCompleteList.add("list");
                        tabCompleteList.add("tp");
                    }
                }
                return tabCompleteList;
            }
        }
        return null;
    }
}
