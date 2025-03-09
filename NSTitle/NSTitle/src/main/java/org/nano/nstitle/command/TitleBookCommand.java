package org.nano.nstitle.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nano.nstitle.controller.TitleBookController;

public class TitleBookCommand implements CommandExecutor {
    private final TitleBookController controller = new TitleBookController();
    /**
     * OP Command ( Only )
     * </ttb give <unique> <amount>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if ( commandSender instanceof Player p && p.isOp()){
            try {
                switch (strings.length) {
                    case 2 -> controller.giveBook(p, strings[1], 1);
                    case 3 -> {
                        int amount = Integer.parseInt(strings[2]);
                        controller.giveBook(p, strings[2], amount);
                    }
                    default -> controller.command(p);
                }
            }catch (Exception e){
                controller.command(p);
            }
        }
        return false;
    }
}
