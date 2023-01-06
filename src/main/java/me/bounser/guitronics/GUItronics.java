package me.bounser.guitronics;

import de.leonhard.storage.util.FileUtils;
import me.bounser.guitronics.advancedgui.AGUIExtension;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.listeners.BlockListener;
import me.bounser.guitronics.listeners.RedstoneListener;
import me.bounser.guitronics.tools.Data;
import me.bounser.guitronics.tools.DebugCommand;
import me.leoko.advancedgui.manager.LayoutManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public final class GUItronics extends JavaPlugin {

    /**
     *  Project Notes:
     *  Analogies:
     *
     *  - Voltage level = Sculk brightness.
     *  - Current (Intensity) doesn't have a parallel model. There's no flow of nothing.
     *
     *  - Potential difference gets translated as brightness difference.
     *
     *  - All the components will follow the ideal behavior, meaning no voltage (brightness) drop inside components.
     *
     *
     *
     *  Technical annotations:
     *
     *  Directions are determined by integers starting with 0 and going clockwise.
     *  (North = 0, East = 1...) -1 means no direction.
     *
     *  Inputs/Outputs follow the same structure.
     */

    private static GUItronics main;
    public static GUItronics getInstance(){ return main; }

    @Override
    public void onEnable() {
        main = this;

        boolean debug = Data.getInstance().getDebug();

        if (debug) getLogger().info("Loading resources...");

        if (Data.getInstance().getCheckLayout()){ checkLayouts(); }

        if (debug) getCommand("debug").setExecutor(new DebugCommand(this));

        LayoutManager.getInstance().registerLayoutExtension(new AGUIExtension(), this);

        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new RedstoneListener(), this);

        if (debug) getLogger().info("Resources loaded.");
        if (debug && CircuitsManager.getInstance().loadCircuits()) getLogger().info("Circuits loaded.");
    }

    public void checkLayouts() {

        getLogger().info("Checking layouts... ");
        getLogger().info("If you want to disable this procedure, set AutoLayoutInjection to false in the config.yml file.");

        for (int i = 0; i <= 3; i++){

            File toLayout0 = new File(getDataFolder().getParent() + "/AdvancedGUI/layout/Circuit" + i + ".json");
            if (toLayout0.exists()){

                InputStream input = null;
                try {
                    input = new FileInputStream(toLayout0);
                } catch (FileNotFoundException e) {
                    getLogger().info("Error trying to read layout " + i + " in AdvancedGUI's layouts folder");
                    e.printStackTrace();
                }
                InputStream fromLayout0 = getResource("Circuit" + i + ".json");

                if(input != fromLayout0){
                    getLogger().info("Layout " + i + " updated.");
                    FileUtils.writeToFile(toLayout0, fromLayout0);
                }

            } else {

                InputStream fromLayout0 = getResource("Circuit" + i + ".json");

                getLogger().info("Layout " + i + " added.");
                FileUtils.writeToFile(toLayout0, fromLayout0);

            }
        }
    }

}
