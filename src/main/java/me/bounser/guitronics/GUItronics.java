package me.bounser.guitronics;

import me.bounser.guitronics.advancedgui.AGUIExtension;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.listeners.BlockListener;
import me.bounser.guitronics.listeners.RedstoneListener;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.manager.LayoutManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class GUItronics extends JavaPlugin {

    private static GUItronics main;
    public static GUItronics getInstance(){ return main; }

    @Override
    public void onEnable() {
        main = this;
        boolean debug = Data.getInstance().getDebug();

        if(debug) getLogger().info("Loading resources...");

        LayoutManager.getInstance().registerLayoutExtension(new AGUIExtension(), this);


        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new RedstoneListener(), this);

        if(debug) getLogger().info("Resources loaded.");

        if(debug && CircuitsManager.getInstance().loadCircuits()) getLogger().info("Circuits loaded.");

    }

}
