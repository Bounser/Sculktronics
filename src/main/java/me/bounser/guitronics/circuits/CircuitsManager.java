package me.bounser.guitronics.circuits;

import me.bounser.guitronics.advancedgui.AGUIInstances;
import me.bounser.guitronics.components.ElectroComponent;
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

    private static CircuitsManager instance;
    public static CircuitsManager getInstance(){
        return instance == null ? instance = new CircuitsManager() : instance;
    }

    public void createCircuit(Location loc, String owneruuid){

        circuits.put(new Circuit(loc, 0, owneruuid, new HashMap<Integer, Object>()), AGUIInstances.getInstance().placeGUI(loc, Direction.FLOOR_NORTH,null, true));
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

        if(circuits.size() != 0){
            for(String uuid : data.getUsersUUID()){
                for(int i = 1; i <= data.getNum(uuid); i++){

                    Circuit cir = new Circuit(data.getLocation(i, uuid), data.getSize(i, uuid), uuid, data.getDesign(uuid));
                    circuits.put(cir, AGUIInstances.getInstance().placeGUI(data.getLocation(i, uuid), Direction.FLOOR_EAST, cir, true));

                }
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
