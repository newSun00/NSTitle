package org.nano.nstitle.manager;

import org.bukkit.entity.Player;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.builder.BookBuilder;

public class TitleBookManager {

    public void giveBook(Player sender, Title title, int amount) {
        sender.getInventory().addItem(
                BookBuilder.builder.book(title,amount)
        );
    }
}
