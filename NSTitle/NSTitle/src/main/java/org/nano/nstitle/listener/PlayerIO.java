package org.nano.nstitle.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nano.nstitle.data.db.FileCore;
import org.nano.nstitle.data.db.file.UserFile;
import org.nano.nstitle.manager.TitleManager;

public class PlayerIO implements Listener {
    private final UserFile userFile = FileCore.getInstance().getUserFile();
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        new TitleManager().compare(p);
        userFile.load(p.getUniqueId());
    }
}
