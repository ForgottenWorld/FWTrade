package me.architetto.fwtrade;

import me.architetto.fwtrade.command.CommandManager;
import me.architetto.fwtrade.listeners.InventoryListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FWTrade extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable(){
        // Plugin startup logic
        plugin = this;

        Objects.requireNonNull(getCommand("fwtrade")).setExecutor(new CommandManager());

        getServer().getPluginManager().registerEvents(new InventoryListener(),this);



    }

    @Override
    public void onDisable(){
        // Plugin shutdown logic
    }
}
