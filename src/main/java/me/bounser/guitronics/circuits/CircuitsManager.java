package me.bounser.guitronics.circuits;

import me.bounser.guitronics.advancedgui.AGUIInstances;
import me.bounser.guitronics.listeners.RedstoneListener;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.manager.GuiWallManager;
import me.leoko.advancedgui.manager.LayoutManager;
import me.leoko.advancedgui.utils.Direction;
import me.leoko.advancedgui.utils.GuiInstance;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Repeater;

import java.awt.*;
import java.util.Map.Entry;
import java.util.HashMap;

public class CircuitsManager {

    // Keeps information about circuits, and it's the responsable of creating/deleting them.

    Data data = Data.getInstance();

    HashMap<Circuit, GuiInstance> circuits = new HashMap<>();
    HashMap<Circuit, List> circuitBases;

    Layout layout = LayoutManager.getInstance().getLayout(data.getLayoutName());

    private static CircuitsManager instance;
    public static CircuitsManager getInstance(){
        return instance == null ? instance = new CircuitsManager() : instance;
    }

    public void createCircuit(Location loc, String owneruuid){

        circuits.put(new Circuit(loc, 1, owneruuid, null, null, null), AGUIInstances.getInstance().placeGUI(loc, Direction.FLOOR_EAST, layout, null, true));
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

                HashMap locations = data.getLocations(uuid);
                circuits.put(new Circuit(locations, uuid, data.getDesign(uuid)),
                        AGUIInstances.getInstance().placeGUI(locations, Direction.FLOOR_EAST, layout));

            }
            return true;
        }
        return false;
    }

    public List getAllCircuits(){
        return (List) circuits.keySet();
    }

    public void updatePuts(Circuit circuit){

        // BASIC CIRCUIT SCHEM.

        if(circuit.getSize() == 0){
            for(int i = 0; i<=4; i++){
                switch(i){
                    case 0:
                        Block block0 = circuit.getLocation().add(0,0,-1).getBlock();
                        if(block0.getType().equals(Material.REPEATER)){
                            Repeater repeater = (Repeater) block0;
                            if(repeater.getFacing().equals(BlockFace.SOUTH)){
                                circuit.addInput(0);
                            } else if(repeater.getFacing().equals(BlockFace.NORTH)){
                                circuit.addOutput(0);
                            }
                        }
                        break;
                    case 1:
                        Block block1 = circuit.getLocation().add(1,0,0).getBlock();
                        if(block1.getType().equals(Material.REPEATER)){
                            Repeater repeater = (Repeater) block1;
                            if(repeater.getFacing().equals(BlockFace.WEST)){
                                circuit.addInput(0);
                            } else if(repeater.getFacing().equals(BlockFace.EAST)){
                                circuit.addOutput(0);
                            }
                        }
                        break;
                    case 2:
                        Block block2 = circuit.getLocation().add(0,0,1).getBlock();
                        if(block2.getType().equals(Material.REPEATER)){
                            Repeater repeater = (Repeater) block2;
                            if(repeater.getFacing().equals(BlockFace.NORTH)){
                                circuit.addInput(0);
                            } else if(repeater.getFacing().equals(BlockFace.SOUTH)){
                                circuit.addOutput(0);
                            }
                        }
                        break;
                    case 3:
                        Block block3 = circuit.getLocation().add(-1,0,0).getBlock();
                        if(block3.getType().equals(Material.REPEATER)){
                            Repeater repeater = (Repeater) block3;
                            if(repeater.getFacing().equals(BlockFace.EAST)){
                                circuit.addInput(0);
                            } else if(repeater.getFacing().equals(BlockFace.WEST)){
                                circuit.addOutput(0);
                            }
                        }
                        break;
                }
            }

        }


    }

}
