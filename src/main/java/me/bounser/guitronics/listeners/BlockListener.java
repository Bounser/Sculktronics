package me.bounser.guitronics.listeners;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.List;

public class BlockListener implements Listener {

    Data data = Data.getInstance();


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        // General block logic

        if(e.getBlock().getBlockData().getMaterial().equals(Material.SCULK_CATALYST) &&
                e.getItemInHand().getItemMeta().getLore() != null &&
                e.getItemInHand().getItemMeta().getLore().contains("circuit")){

            // Check for near circuits

            for(List<Location> listLocation : data.getAllCircuitLocations().values()){

                for(Location loc : listLocation){
                    if(loc.distance(e.getBlock().getLocation()) < 10){
                        e.getPlayer().sendMessage("Its too close to another circuit.");
                        e.setCancelled(true);
                        return;
                    }
                }

            }

            // Create new circuit

            CircuitsManager.getInstance().createCircuit(e.getBlock().getLocation(), e.getPlayer().getUniqueId().toString());
            return;
        }

    }

    public void checkCircuitPuts(Circuit circuit){

        HashMap<Location, Integer> locations = new HashMap<>();

        locations.put(circuit.getLocation().add(0,0,-1), 0);

        if(circuit.getSize() == 0){
            locations.put(circuit.getLocation().add(1,0,0), 1);
            locations.put(circuit.getLocation().add(0,0,-1), 2);
            locations.put(circuit.getLocation().add(-1,0,0), 3);
        }
        if(circuit.getSize() == 1){
            locations.put(circuit.getLocation().add(1,0,-1), 1);
            locations.put(circuit.getLocation().add(2,0,0), 2);
            locations.put(circuit.getLocation().add(1,0,1), 3);
            locations.put(circuit.getLocation().add(0,0,1), 4);
            locations.put(circuit.getLocation().add(-1,0,0), 5);
        }
        if(circuit.getSize() == 2){
            locations.put(circuit.getLocation().add(1,0,0), 1);
            locations.put(circuit.getLocation().add(1,0,1), 2);
            locations.put(circuit.getLocation().add(0,0,2), 3);
            locations.put(circuit.getLocation().add(-1,0,1), 4);
            locations.put(circuit.getLocation().add(-1,0,0), 5);
        }
        if(circuit.getSize() == 3){
            locations.put(circuit.getLocation().add(1,0,-1), 1);
            locations.put(circuit.getLocation().add(2,0,0), 2);
            locations.put(circuit.getLocation().add(2,0,-1), 3);
            locations.put(circuit.getLocation().add(1,0,-2), 4);
            locations.put(circuit.getLocation().add(0,0,-2), 5);
            locations.put(circuit.getLocation().add(-1,0,1), 6);
            locations.put(circuit.getLocation().add(-1,0,0), 7);
        }

        // FOR CIRCUITS SIZED FROM 1-3

        for(Location loc : locations.keySet()){

            Block block = loc.getBlock();
            Block blockC = getNearestLoc(loc, circuit.getLocations()).getBlock();

            if(block.getType().equals(Material.REPEATER)){
                Repeater repeater = (Repeater) block;

                if(blockC.getFace(block) == BlockFace.EAST){

                    if(repeater.getFacing() == BlockFace.EAST){
                        circuit.addInput(locations.get(loc));
                    } else if(repeater.getFacing() == BlockFace.WEST){
                        circuit.addOutput(locations.get(loc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.WEST){

                    if(repeater.getFacing() == BlockFace.WEST){
                        circuit.addInput(locations.get(loc));
                    } else if(repeater.getFacing() == BlockFace.EAST){
                        circuit.addOutput(locations.get(loc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.NORTH){

                    if(repeater.getFacing() == BlockFace.NORTH){
                        circuit.addInput(locations.get(loc));
                    } else if(repeater.getFacing() == BlockFace.SOUTH){
                        circuit.addOutput(locations.get(loc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.SOUTH){

                    if(repeater.getFacing() == BlockFace.SOUTH){
                        circuit.addInput(locations.get(loc));
                    } else if(repeater.getFacing() == BlockFace.NORTH){
                        circuit.addOutput(locations.get(loc));
                    }

                }
            }
        }
    }

    public Location getNearestLoc(Location loc, List<Location> locs){

        // Indeed, although the name might be misleading, it returns the block with distance 1.

        for(Location l : locs){
            if(l.distance(loc) == 1){
                return l;
            }
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

    }
}
