package org.nano.nstitle.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nano.nstitle.data.db.FileCore;

public class PlayerIO implements Listener {
    private final FileCore core = FileCore.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player p = e.getPlayer();
        if ( core.getConfig().isEnable()) {
            core.getDataSQL().update();
            core.getDataSQL().load();
        }
        core.getUserFile().load(p.getUniqueId());
    }
}
