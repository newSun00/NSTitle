package org.nano.nstitle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nano.nstitle.command.TitleBookCommand;
import org.nano.nstitle.command.TitleBookCompleter;
import org.nano.nstitle.command.TitleCommand;
import org.nano.nstitle.command.TitleCompleter;
import org.nano.nstitle.listener.PlayerIO;
import org.nano.nstitle.manager.DatabaseManager;
import org.nano.nstitle.placeholder.PlayerPlaceHolder;
import org.nano.nstitle.placeholder.TitlePlaceFirstHolder;
import org.nano.nstitle.placeholder.TitlePlaceSecondHolder;
import org.nano.nstitle.placeholder.TitlePlaceThirdHolder;

import java.util.Objects;

public final class NSTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("tt")).setExecutor(new TitleCommand());
        Objects.requireNonNull(getCommand("tt")).setTabCompleter(new TitleCompleter());

        Objects.requireNonNull(getCommand("ttb")).setExecutor(new TitleBookCommand());
        Objects.requireNonNull(getCommand("ttb")).setTabCompleter(new TitleBookCompleter());

        getServer().getPluginManager().registerEvents(new PlayerIO(),this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")){
            new TitlePlaceFirstHolder().register();
            new TitlePlaceSecondHolder().register();
            new TitlePlaceThirdHolder().register();
            new PlayerPlaceHolder().register();
        }

    }

    @Override
    public void onDisable() {
        DatabaseManager.getInstance().close();
    }
}
