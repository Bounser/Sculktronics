package me.bounser.guitronics.listeners;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.List;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        // BASE BLOCK

        if(e.getBlock().getBlockData().getMaterial().equals(Material.SCULK_CATALYST) &&
                e.getItemInHand().getItemMeta().getLore() != null &&
                e.getItemInHand().getItemMeta().getLore().contains("circuit")){

            // Check for near circuits

            for(Location loc : CircuitsManager.getInstance().getCircuitLocs().values()){

                if(loc.distance(e.getBlock().getLocation()) < 10){
                    e.getPlayer().sendMessage("Its too close to another circuit.");
                    e.setCancelled(true);
                    return;
                }
            }

            // Create new circuit

            CircuitsManager.getInstance().createCircuit(e.getBlock().getLocation().add(0,1,0), e.getPlayer().getUniqueId().toString());
        }

        // REPEATER

        if(e.getBlock().getBlockData().getMaterial().equals(Material.REPEATER)){

            HashMap<Circuit, Location> cirLocs = CircuitsManager.getInstance().getCircuitLocs();

            for(Circuit cir : cirLocs.keySet()){

                if(cir.getPutsLocations().containsKey(e.getBlock().getLocation())){
                    checkCircuitPuts(cir);
                    cir.updatePuts();
                }

            }

        }

    }

    public void checkCircuitPuts(Circuit circuit){

        HashMap<Location, Integer> plocs = circuit.getPutsLocations();

        // FOR CIRCUITS SIZED FROM 1-3

        for(Location ploc : plocs.keySet()){

            if(ploc.getBlock().getType() == Material.REPEATER){
                Block block = ploc.getBlock();
                Block blockC = getNearestLoc(ploc, circuit.getLocations()).getBlock();
                Directional repeater = null;

                if(block.getBlockData().getMaterial().equals(Material.REPEATER) && Data.getInstance().getDebug()){
                    Bukkit.broadcastMessage("Its a repeater!");
                    repeater = (Directional) block.getBlockData();
                }

                if(Data.getInstance().getDebug()) Bukkit.broadcastMessage(blockC.getLocation() + " " + blockC.getFace(block));

                if(blockC.getFace(block) == BlockFace.EAST){

                    if(repeater.getFacing() == BlockFace.EAST){
                        circuit.addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.WEST){
                        circuit.addOutput(plocs.get(ploc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.WEST){

                    if(repeater.getFacing() == BlockFace.WEST){
                        circuit.addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.EAST){
                        circuit.addOutput(plocs.get(ploc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.NORTH){

                    if(repeater.getFacing() == BlockFace.NORTH){
                        circuit.addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.SOUTH){
                        circuit.addOutput(plocs.get(ploc));
                    }

                }

                if(blockC.getFace(block) == BlockFace.SOUTH){

                    if(repeater.getFacing() == BlockFace.SOUTH){
                        circuit.addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.NORTH){
                        circuit.addOutput(plocs.get(ploc));
                    }

                }
            }



        }
    }

    public Location getNearestLoc(Location loc, List<Location> locs){

        // Indeed, although the name might be misleading without context, it returns one block of the list with distance 1.

        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage(loc + " " + locs);

        for(Location l : locs){
            l.add(0,-1,0);
            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage(String.valueOf(l.distance(loc)));
            if(l.distance(loc) == 1){
                if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Returned.");
                return l;
            }
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        if(e.getBlock().getType() == Material.SCULK_CATALYST){
            for(Circuit cir : CircuitsManager.getInstance().getAllCircuits()){

                if(cir.getLocations().contains(e.getBlock().getLocation().add(0,1,0))){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "This block is from a circuit!");
                }
            }
        }

        if(e.getBlock().getType() == Material.REPEATER){

            for(Circuit cir : CircuitsManager.getInstance().getAllCircuits()){

                Bukkit.broadcastMessage(cir.getPutsLocations() + " " + e.getBlock().getLocation());

                if(cir.getPutsLocations().containsKey(e.getBlock().getLocation())){

                    Bukkit.broadcastMessage("ASD");
                    cir.removePut(e.getBlock().getLocation());

                }
            }
        }
    }
}
