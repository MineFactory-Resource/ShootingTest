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
                    tabCompleteList.add("리로드");
                    tabCompleteList.add("지역");
                    tabCompleteList.add("도움말");
                    tabCompleteList.add("완드");
                    tabCompleteList.add("타겟");
                }
                if (args[0].equalsIgnoreCase("지역")) {
                    if (args.length > 1) {
                        if (args.length == 2) {
                            tabCompleteList.add("생성");
                            tabCompleteList.add("제거");
                            tabCompleteList.add("확인");
                        } else if (args.length == 3 && args[1].equals("확인")) {
                            tabCompleteList.add("목록");
                            tabCompleteList.add("위치");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("타겟")) {
                    if (args.length == 2) {
                        tabCompleteList.add("생성");
                        tabCompleteList.add("제거");
                        tabCompleteList.add("목록");
                        tabCompleteList.add("텔레포트");
                    }
                }
                return tabCompleteList;
            }
        }
        return null;
    }
}
