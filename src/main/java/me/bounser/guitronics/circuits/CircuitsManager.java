package me.bounser.guitronics.circuits;

import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.GuiInstance;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;

public class CircuitsManager {

    // Responsable of creating/deleting circuits and keeping record of things.

    List<Circuit> circuits = new ArrayList<>();

    private static CircuitsManager instance;
    public static CircuitsManager getInstance(){
        return instance == null ? instance = new CircuitsManager() : instance;
    }

    public void createCircuit(Location loc, String owneruuid){

        circuits.add(new Circuit(loc, 0, owneruuid, new HashMap<>(), Data.getInstance().getNum(owneruuid) +1));
        Data.getInstance().registerCircuit(loc, owneruuid);

    }

    public Circuit getCircuitFromGUIInstance(GuiInstance guiinstance){
        for(Circuit cir : circuits){
            if(cir.getGuiWallInstance() == guiinstance) return cir;
        }
        return null;
    }

    public Circuit getCircuitFromInteraction(Interaction interaction){
        for(Circuit cir: circuits){
            if(cir.getInteractions().contains(interaction)) return cir;
        }
        return null;
    }

    public boolean loadCircuits(){

        Data data = Data.getInstance();

        if(circuits.size() == 0){
            for(String uuid : data.getUsersUUID()){
                for(int i = 0; i <= data.getNum(uuid); i++){

                    circuits.add(new Circuit(data.getLocation(i, uuid), data.getSize(i, uuid), uuid, data.getDesign(i, uuid), i));

                }
            }
            return true;
        }
        return false;
    }

    public Circuit getCircuitFromOwner(int num, String UUID){
        for(Circuit cir : circuits){
            if(cir.getOwneruuid().equals(UUID) && cir.getNum() == num){
                return cir;
            }
        }
        return null;
    }

    public Collection<Circuit> getAllCircuits(){ return circuits; }

    public HashMap<Circuit, Location> getCircuitLocs(){

        HashMap<Circuit, Location> circuitBases = new HashMap<>();

        for(Circuit cir : circuits)
            circuitBases.put(cir, cir.getLocation());


        return circuitBases;
    }

    public int getRoundFromEComponent(EComponent EComponent){

        switch(EComponent){
            case WIRE: return 0;
            case DIODE:
            case NOT:
                return 3;
            case RESISTOR: return 4;
        }
        return 0;
    }

}
