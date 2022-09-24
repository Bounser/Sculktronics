package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.manager.GuiWallManager;
import me.leoko.advancedgui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class AGUIInstances {

    private static AGUIInstances instance;
    public static AGUIInstances getInstance(){
        return instance == null ? instance = new AGUIInstances() : instance;
    }

    // Places the AdvancedGUI GUI and returns its GUIinstance.
    public GuiInstance placeGUI(Location loc, Direction dir, Layout layout){

        GuiWallManager gwm = GuiWallManager.getInstance();

        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Placing GUI... " + (gwm.getActiveInstance(loc) == null ? "null" : "true"));

        if(gwm.getActiveInstance(loc) == null){

            if(!loc.getChunk().isLoaded()) loc.getChunk().load();

            ItemFrame itemFrame = (ItemFrame) loc.getWorld().spawnEntity(new Location(loc.getWorld(), 0,0,0), EntityType.ITEM_FRAME);
            itemFrame.setVisible(false);
            itemFrame.teleport(loc);
            itemFrame.setFacingDirection(BlockFace.UP, true);

            GuiWallInstance guiInstance = new GuiWallInstance(gwm.getNextId(), layout, 3, new GuiLocation(loc, dir));
            gwm.registerInstance(guiInstance, true);

            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("GUI Placed with instance: " + guiInstance);
            return guiInstance;
        }
        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("There was already an instance there");
        return gwm.getActiveInstance(loc);
    }


}
