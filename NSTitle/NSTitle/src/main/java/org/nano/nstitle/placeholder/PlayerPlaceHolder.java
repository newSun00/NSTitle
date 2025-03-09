package org.nano.nstitle.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerPlaceHolder extends PlaceholderExpansion {

    @Override
    @NotNull
    public String getAuthor() {
        return "Author"; //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "title-player";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params){
        if ( p == null ) return "";
        if ( params.equals("title-player")) return getAuthor();
        return p.getName();
    }
}
