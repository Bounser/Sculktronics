package me.bounser.guitronics.listeners;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import java.util.HashMap;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        // BASE BLOCK

        if (e.getBlock().getBlockData().getMaterial().equals(Material.SCULK_CATALYST) &&
                e.getItemInHand().getItemMeta().getLore() != null &&
                e.getItemInHand().getItemMeta().getLore().contains("circuit")){

            // Check for near circuits

            for (Location loc : CircuitsManager.getInstance().getCircuitLocs().values()){

                if (loc.distance(e.getBlock().getLocation()) == 1){

                    for (Circuit cir : CircuitsManager.getInstance().getAllCircuits()){
                        if (cir.getLocation().equals(loc)){

                        // TODO - Logic for physical extensions. On second expansion: check for needed materials.

                        }
                    }

                }

                if (loc.distance(e.getBlock().getLocation()) < 10){
                    e.getPlayer().sendMessage("Its too close to another circuit.");
                    e.setCancelled(true);
                    return;
                }
            }

            // New circuit gets created

            CircuitsManager.getInstance().createCircuit(e.getBlock().getLocation().add(0,1,0), e.getPlayer().getUniqueId().toString());
        }

        // REPEATER

        if (e.getBlock().getBlockData().getMaterial().equals(Material.REPEATER)){

            HashMap<Circuit, Location> cirLocs = CircuitsManager.getInstance().getCircuitLocs();

            for (Circuit cir : cirLocs.keySet()){

                if (cir.getPutsLocations().containsKey(e.getBlock().getLocation())){
                    cir.checkCircuitPuts();
                    cir.updatePuts();
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        if (e.getBlock().getType() == Material.SCULK_CATALYST){
            for (Circuit cir : CircuitsManager.getInstance().getAllCircuits()){

                if (cir.getLocations().contains(e.getBlock().getLocation().add(0,1,0))){
                    if (e.getPlayer().getUniqueId().toString().equals(cir.getOwnerUUID())){
                        if (e.getPlayer().isSneaking()){
                            cir.dispose();
                            e.getPlayer().sendMessage(ChatColor.GRAY + "Circuit removed.");
                        } else {
                            e.getPlayer().sendMessage("That block is from a circuit! If you want to break it, sneak while trying to break it.");
                        }
                    }
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "This block is from a circuit!");
                }
            }
        }

        if(e.getBlock().getType() == Material.REPEATER){

            for(Circuit cir : CircuitsManager.getInstance().getAllCircuits()){

                if(cir.getPutsLocations().containsKey(e.getBlock().getLocation())){

                    cir.removePut(cir.getPutFromLoc(e.getBlock().getLocation()));

                }
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockFormEvent e){

    }

    @EventHandler
    public void onBlockChange(BlockSpreadEvent e){

    }

}
