package net.teamuni.shootingtest.Command;

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
            if (args.length == 1) {
                tabCompleteList.add("위치설정");
            }
            return tabCompleteList;
        }
        return null;
    }
}
