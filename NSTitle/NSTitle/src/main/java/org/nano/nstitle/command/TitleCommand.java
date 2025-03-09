package org.nano.nstitle.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nano.nstitle.controller.TitleController;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.LoreKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleCommand implements CommandExecutor {

    private final TitleController controller = new TitleController();


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if ( strings.length == 0 ){
            if ( commandSender instanceof Player p ){
                if ( p.isOp() ) controller.commandListOp(p);
                controller.commandListUser(p);
            }
        }
        if ( strings.length > 0 ) {
            try {
                console(commandSender, strings);
                if (op(commandSender, strings)) {
                    return false;
                }
                user(commandSender, strings);
            }catch (Exception ignored){

            }
        }

        return false;
    }


    private void console(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender sender) {
            switch ( strings[0] ) {
                case "remove" -> controller.removeTitleByPlayer(sender, strings[1], strings[2]);
                case "delete" -> controller.delete(sender, strings[1]);
                case "give" -> controller.giveTitleByPlayer(sender, strings[1], strings[2]);
            }
        }
    }

    private boolean op(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof Player sender && sender.isOp()) {
            try {
                switch (strings[0]) {
                    case "create" -> {
                        controller.create(sender, strings[1]);
                        return true;
                    }
                    case "delete" -> {
                        controller.delete(sender, strings[1]);
                        return true;
                    }
                    case "give" -> {
                        controller.giveTitleByPlayer(sender, strings[1], strings[2]);
                        return true;
                    }
                    case "remove" -> {
                        controller.removeTitleByPlayer(sender, strings[1], strings[2]);
                        return true;
                    }
                    case "set" -> {
                        switch (strings[2]) {
                            case "lore" -> {
                                String unique = strings[1];
                                switch (strings[3]) {
                                    case "add" -> {
                                        LoreKeys key = LoreKeys.ADD;
                                        // lore add <line> <string>
                                        if ( strings.length == 6 ) {
                                            int line = Integer.parseInt(strings[4]);
                                            String str = String.join(" ", Arrays.copyOfRange(strings, 5, strings.length));
                                            controller.lore(sender,unique,key,str,line);
                                        }else{
                                            String str = String.join(" ", Arrays.copyOfRange(strings, 5, strings.length));
                                            controller.lore(sender,unique,key,str,-99);
                                        }
                                        return true;
                                    }
                                    case "remove" -> {
                                        LoreKeys key = LoreKeys.REMOVE;
                                        int line = Integer.parseInt(strings[4]);
                                        controller.lore(sender,unique,key,"",line);
                                        return true;
                                    }
                                    case "clear" -> {
                                        LoreKeys key = LoreKeys.CLEAR;
                                        controller.lore(sender,unique,key,strings[5],-99);
                                        return true;
                                    }
                                }
                            }
                            case "col" -> {
                                String unique = strings[1];
                                int num = Integer.parseInt(strings[3]);
                                controller.col(sender,unique,num);
                                return true;
                            }
                            case "special-color" -> {
                                String unique = strings[1];
                                ColorKeys type = ColorKeys.valueOf(strings[3].toUpperCase());
                                switch (type){
                                    case GRADIENT -> {
                                        List<String> hex = new ArrayList<>();
                                        hex.add(strings[4]);
                                        hex.add(strings[5]);
                                        hex.add(strings[6]);
                                        controller.color(sender,unique,ColorKeys.GRADIENT,hex);
                                    }
                                    case RAINBOW -> controller.color(sender,unique,ColorKeys.RAINBOW,new ArrayList<>());
                                    case NONE -> controller.color(sender,unique,ColorKeys.NONE,new ArrayList<>());
                                }
                                return true;
                            }
                            case "name"->{
                                String unique = strings[1];
                                String name = String.join(" ", Arrays.copyOfRange(strings, 3, strings.length));
                                controller.display(sender,unique,name);
                                return true;
                            }
                        }
                    }
                    default -> controller.commandListOp(sender);
                }
            } catch (Exception e) {
                e.printStackTrace();
                controller.commandListOp(sender);
            }
        }
        return false;
    }

    private void user(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof Player sender) {
            if ( strings[0].equals("list")){
                controller.list(sender,sender.getName());
            }
            else if ( strings[0].equals("apply")){
                controller.equip(sender,sender.getName(),strings[1]);
            }
            else controller.commandListUser(sender);
        }
    }
}
