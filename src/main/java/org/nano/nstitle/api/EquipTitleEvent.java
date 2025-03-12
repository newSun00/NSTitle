package org.nano.nstitle.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.nano.nstitle.data.registy.server.dto.Title;

public class EquipTitleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Title title;
    private final Player player;
    private boolean cancelled;

    public EquipTitleEvent(Player player, Title title) {
        this.player = player;
        this.title = title;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Title getTitle() {
        return title;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
