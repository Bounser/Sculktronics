package me.bounser.guitronics.listeners;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Location;
import org.bukkit.Material;
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

            Location locbase = e.getBlock().getLocation();
            Location loc = e.getBlock().getLocation().add(0,1,0);

            // Create new circuit

            CircuitsManager.getInstance().createCircuit(e.getBlock().getLocation(), e.getPlayer().getUniqueId().toString());
            return;
        }

        if(e.getBlock().getBlockData().getMaterial().equals(Material.REPEATER)){

            for(Circuit cir : CircuitsManager.getInstance().getAllCircuits()){

            }

            CircuitsManager.getInstance().updatePuts();

        }

    }

    public void checkCircuitPuts(Circuit circuit){

        HashMap<Integer, Location> locations = circuit.getLocation();


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

    }
}
