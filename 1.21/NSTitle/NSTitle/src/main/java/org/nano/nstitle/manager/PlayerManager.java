package org.nano.nstitle.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {
    public Player getTarget(String target) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getName().equals(target))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("플레이어를 찾을 수 없습니다. : " + target));
    }
}
