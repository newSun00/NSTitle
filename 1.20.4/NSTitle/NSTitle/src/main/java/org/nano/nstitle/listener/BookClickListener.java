package org.nano.nstitle.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.nano.nstitle.controller.TitleBookController;
import org.nano.nstitle.util.helper.namespace.KeyHelper;

public class BookClickListener implements Listener {
    private final KeyHelper keyHelper = new KeyHelper();
    @EventHandler
    public void bookClick(PlayerInteractEvent e){
        Player sender = e.getPlayer();
        clickTitleBook(sender,e);
    }

    private void clickTitleBook(Player sender, PlayerInteractEvent e) {
        if ( e.getAction().isRightClick()) {
            boolean b = keyHelper.hasKey(e.getItem(), "unique");
            if (b) {
                String unique = keyHelper.getValue(e.getItem(), "unique");
                new TitleBookController().clickTitleBook(e.getItem(), sender, unique);
            }
        }
    }
}
