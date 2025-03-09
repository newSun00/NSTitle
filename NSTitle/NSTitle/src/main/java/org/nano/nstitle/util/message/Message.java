package org.nano.nstitle.util.message;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.nano.nstitle.util.factory.ColorFactory;

public class Message {
    public static void message(ConsoleCommandSender sender, String s){
        sender.sendMessage(ColorFactory.factory
                .color(s)
                .doneC());
    }
    public static void message(Player sender, String s){
        sender.sendMessage(ColorFactory.factory
                .color(s)
                .doneC());
    }
}
