package me.bounser.guitronics.circuits;

import me.bounser.guitronics.advancedgui.AGUIInstances;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.Direction;
import me.leoko.advancedgui.utils.GuiInstance;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Location;

import java.util.List;
import java.util.Map.Entry;
import java.util.HashMap;

public class CircuitsManager {

    // Keeps information about circuits, and it's the responsable of creating/deleting them and keeping record of things.

    HashMap<Circuit, GuiInstance> circuits = new HashMap<>();

    private static CircuitsManager instance;
    public static CircuitsManager getInstance(){
        return instance == null ? instance = new CircuitsManager() : instance;
    }

    public void createCircuit(Location loc, String owneruuid){

        circuits.put(new Circuit(loc, 0, owneruuid, new HashMap<>(), Data.getInstance().getNum(owneruuid) +1), AGUIInstances.getInstance().placeGUI(loc, Direction.FLOOR_NORTH,null, true));
        Data.getInstance().registerCircuit(loc, owneruuid);

    }

    public void updateCircuit(Circuit cir){

        circuits.replace(cir, AGUIInstances.getInstance().placeGUI(cir.getLocation(), Direction.FLOOR_NORTH, cir, true));

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

    public boolean loadCircuits(){

        Data data = Data.getInstance();

        if(circuits.size() != 0){
            for(String uuid : data.getUsersUUID()){
                for(int i = 1; i <= data.getNum(uuid); i++){

                    Circuit cir = new Circuit(data.getLocation(i, uuid), data.getSize(i, uuid), uuid, data.getDesign(uuid), i);
                    circuits.put(cir, AGUIInstances.getInstance().placeGUI(data.getLocation(i, uuid), Direction.FLOOR_EAST, cir, true));

                }
            }
            return true;
        }
        return false;
    }

    public List<Circuit> getAllCircuits(){ return (List<Circuit>) circuits.keySet(); }

    public HashMap<Circuit, Location> getCircuitLocs(){

        HashMap<Circuit, Location> circuitBases = new HashMap<>();

        for(Circuit cir : circuits.keySet())
            circuitBases.put(cir, cir.getLocation());


        return circuitBases;
    }

    public int getRoundFromEComponent(EComponent EComponent){

        switch(EComponent){
            case WIRE: return 0;
            case DELAYER:
            case DIODE:
            case INVERTER:
                return 3;
            case RESISTOR: return 4;
        }
        return 0;
    }

}
