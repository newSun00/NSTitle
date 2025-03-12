package org.nano.nstitle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nano.nstitle.data.registy.RegistryCore;

import java.util.ArrayList;
import java.util.List;

public class TitleBookCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        if (commandSender instanceof Player p && p.isOp()) {
            // 첫 번째 인수: 유니크 제목 목록 제공
            if (strings.length == 1) {
                completions.addAll(getAllUniqueTitles());
            }
            // 두 번째 인수: 온라인 플레이어 목록 제공
            else if (strings.length == 2) {
                completions.addAll(getAllPlayerNames());
            }
            // 세 번째 인수: 숫자(아이템 개수) 제공
            else if (strings.length == 3) {
                completions.add("1 ~ 64"); // 기본값 1
            }
        }

        return completions;
    }

    private List<String> getAllUniqueTitles() {
        List<String> uniqueName = new ArrayList<>();
        RegistryCore.getInstance().getTitleRegistry().getTitles()
                .forEach(title -> uniqueName.add(title.getUnique()));
        return uniqueName;
    }

    private List<String> getAllPlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    private List<String> getPlayerOwnedTitles(Player player) {
        return RegistryCore.getInstance().getUserRegistry().gettInv(player.getUniqueId())
                .getHaveTitles()
                .stream()
                .toList();
    }
}
