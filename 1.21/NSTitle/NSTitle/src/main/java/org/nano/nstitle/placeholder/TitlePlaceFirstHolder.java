package org.nano.nstitle.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.data.registy.user.UserRegistry;
import org.nano.nstitle.data.registy.user.dto.TitleInventory;
import org.nano.nstitle.manager.TitleManager;

public class TitlePlaceFirstHolder extends PlaceholderExpansion {
    private final UserRegistry registry = RegistryCore.getInstance().getUserRegistry();
    private final TitleManager manager = new TitleManager();
    @Override
    @NotNull
    public String getAuthor() {
        return "Author"; //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "title-1"; //
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0"; //
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params){
        if ( p == null ) return "";
        if ( params.equals("title-1")) return getAuthor();
        TitleInventory inv = registry.gettInv(p.getUniqueId());
        String name = "";
        if ( inv.getEquip(1) != null ) {
            Title title = manager.getTitle(inv.getEquip(1));
            Color color = title.getColor();
            name = color.getDisplay();
        }
        return name;
    }
}
