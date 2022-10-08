package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.manager.GuiWallManager;
import me.leoko.advancedgui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AGUIInstances {

    private static AGUIInstances instance;
    public static AGUIInstances getInstance(){
        return instance == null ? instance = new AGUIInstances() : instance;
    }

    // Places the AdvancedGUI GUI and returns its GUIinstance.
    public GuiInstance placeGUI(Location location, Direction dir, Layout layout, Circuit cir, Boolean remove){

        GuiWallManager gwm = GuiWallManager.getInstance();

        List<Location> locations = new ArrayList<Location>();
        locations.add(location);

        switch(cir.getSize()){

            case 1:
                locations.add(location.add(1,0,0)); break;
            case 2:
                locations.add(location.add(0,0,1)); break;
            case 3:
                locations.add(location.add(1,0,0));
                locations.add(location.add(0,0,1));
                locations.add(location.add(1,0,1));

        }


        for(Location loc : locations){
            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Placing GUI... " + (gwm.getActiveInstance(loc) == null ? "null" : "true"));
            if(!loc.getChunk().isLoaded()) loc.getChunk().load();

            if(remove) gwm.getActiveInstance(loc).dispose();

            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5)){
                if(entity instanceof ItemFrame){
                    entity.remove();
                }
            }

            ItemFrame itemFrame = (ItemFrame) loc.getWorld().spawnEntity(new Location(loc.getWorld(), 0,0,0), EntityType.ITEM_FRAME);
            itemFrame.setVisible(false);
            itemFrame.teleport(loc);
            itemFrame.setFacingDirection(BlockFace.UP, true);
        }

        if(gwm.getActiveInstance(locations.get(0)) == null){

            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("There was already an instance there");
            return gwm.getActiveInstance(locations.get(0));

        } else {

            GuiWallInstance guiInstance = new GuiWallInstance(gwm.getNextId(), layout, 3, new GuiLocation(locations.get(0), dir));
            gwm.registerInstance(guiInstance, true);

            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("GUI Placed with instance: " + guiInstance);
            return guiInstance;
        }
    }

}
