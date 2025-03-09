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
import java.util.stream.Stream;

public class TitleCompleter implements TabCompleter {

    private final List<String> MAIN_COMMANDS = List.of("create", "set", "delete", "give", "remove", "reload");
    private final List<String> SET_SUBCOMMANDS = List.of("name", "lore", "col", "special-color");
    private final List<String> LORE_SUBCOMMANDS = List.of("add", "remove", "clear");
    private final List<String> COLOR_TYPES = List.of("GRADIENT", "RAINBOW", "NONE");
    private final List<String> LINE_NUMBERS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player p) {
            boolean isOp = p.isOp();

            if (args.length == 1) {
                List<String> userCommands = List.of("list", "apply");

                return isOp
                        ? Stream.concat(userCommands.stream(), MAIN_COMMANDS.stream())
                        .filter(cmd -> cmd.startsWith(args[0]))
                        .toList()
                        : userCommands.stream()
                        .filter(cmd -> cmd.startsWith(args[0]))
                        .toList();
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("apply")) {
                return getPlayerOwnedTitles(p).stream()
                        .filter(title -> title.startsWith(args[1]))
                        .toList();
            }
            // OP 전용 명령어 처리
            if (isOp) {
                if (args.length == 2) {
                    return switch (args[0].toLowerCase()) {
                        case "set", "delete", "apply" -> getAllUniqueTitles().stream()
                                .filter(title -> title.startsWith(args[1]))
                                .toList();
                        case "give", "remove", "list" -> getAllPlayerNames().stream()
                                .filter(name -> name.startsWith(args[1]))
                                .toList();
                        default -> List.of();
                    };
                }

                if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                    return SET_SUBCOMMANDS.stream()
                            .filter(sub -> sub.startsWith(args[2]))
                            .toList();
                }

                if (args.length == 3 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove"))) {
                    return getAllUniqueTitles().stream()
                            .filter(sub -> sub.startsWith(args[2]))
                            .toList();
                }

                if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("lore")) {
                        return LORE_SUBCOMMANDS.stream()
                                .filter(sub -> sub.startsWith(args[3]))
                                .toList();
                    }
                    if (args[2].equalsIgnoreCase("col")) {
                        return List.of("1", "2", "3");
                    }
                    if (args[2].equalsIgnoreCase("special-color")) {
                        return COLOR_TYPES.stream()
                                .filter(type -> type.startsWith(args[3]))
                                .toList();
                    }
                }

                if (args.length == 5 && args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("lore")) {
                    if (args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("remove")) {
                        return LINE_NUMBERS.stream()
                                .filter(num -> num.startsWith(args[4]))
                                .toList();
                    }
                }

                if (args.length >= 5 && args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("special-color") && args[3].equalsIgnoreCase("GRADIENT")) {
                    return List.of("#HEX", "#HEX", "#HEX");
                }
            }
        }
        return List.of();
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