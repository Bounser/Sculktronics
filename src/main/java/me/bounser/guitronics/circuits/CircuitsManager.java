package me.bounser.guitronics.circuits;

import me.bounser.guitronics.advancedgui.AGUIInstances;
import me.bounser.guitronics.listeners.RedstoneListener;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.Direction;
import me.leoko.advancedgui.utils.GuiInstance;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Location;

import java.awt.*;
import java.util.Map.Entry;
import java.util.HashMap;

public class CircuitsManager {

    // Keeps information about circuits, and it's the responsable of creating/deleting them.

    Data data = Data.getInstance();

    HashMap<Circuit, GuiInstance> circuits = new HashMap<>();
    HashMap<Circuit, List> circuitBases;
    Layout layout = Data.getInstance().getLayout();

    private static CircuitsManager instance;
    public static CircuitsManager getInstance(){
        return instance == null ? instance = new CircuitsManager() : instance;
    }

    public void createCircuit(HashMap loc, String owneruuid){

        circuits.put(new Circuit(loc, owneruuid, null, null, null), AGUIInstances.getInstance().placeGUI(loc, Direction.FLOOR_EAST, layout));
        Data.getInstance().registerCircuit(loc, owneruuid);

    }

    public Circuit getCircuitFromGUIInstance(GuiInstance guiinstance){
        for(Entry<Circuit, GuiInstance> e: circuits.entrySet()){
            if(e.getValue() == guiinstance) return e.getKey();
        }
        return null;
    }

    public Circuit getCircuitFromInteraction(Interaction interaction){
        for(Circuit cir: circuits.keySet()){
            if(cir.getInteractions().contains(interaction)) return cir;
        }
        return null;
    }

    public Circuit getCircuitFromBaseLocation(Location circuitBase){
        for(Circuit circuit : circuits.keySet()){
            for(Location loc : circuit.getLocation().values()){
                if(loc.equals(circuitBase)) return circuit;
            }
        }
        return null;
    }

    public Circuit getCircuitFromOwner(String uuid){
        for(Circuit cir : circuits.keySet()){
            if(cir.getOwneruuid().equals(uuid)){
                return cir;
            }
        }
        return null;
    }

    public boolean loadCircuits(){

        Data data = Data.getInstance();

        if(!(circuits.size() == 0)){
            for(String uuid : data.getUsersUUID()){

                HashMap Location loc = data.getLocation(uuid);
                circuits.put(new Circuit(loc, uuid, data.getDesign(uuid)),
                        AGUIInstances.getInstance().placeGUI(loc, Direction.FLOOR_EAST, layout));
                RedstoneListener.getInstance().addBase(loc.add(0,-1,0));

            }
            return true;
        }
        return false;
    }
}
