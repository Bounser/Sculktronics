package me.bounser.guitronics.listeners;

import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BlockListener implements Listener {

    Data data = Data.getInstance();


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        if(e.getBlock().getBlockData().getMaterial().equals(Material.SCULK_CATALYST) &&
                e.getItemInHand().getItemMeta().getLore() != null &&
                e.getItemInHand().getItemMeta().getLore().contains("circuit")){

            for(Location circuitLocation : data.getCircuitsLoc()){
                if(circuitLocation.distance(e.getBlock().getLocation()) < 10){
                    e.getPlayer().sendMessage("Its too close to another one.");
                    e.setCancelled(true);
                    return;
                }
            }

            Location locbase = e.getBlock().getLocation();
            Location loc = e.getBlock().getLocation().add(0,1,0);

            CircuitsManager.getInstance().createCircuit(e.getBlock().getLocation().add(0,1,0), e.getPlayer().getUniqueId().toString());
            data.addCircuitBases(locbase);
            data.addCircuitLoc(loc);
            return;
        }

        if(e.getBlock().getBlockData().getMaterial().equals(Material.REPEATER)){

            for(Location loc : data.getCircuitBases()){
                if(loc.distance(e.getBlock().getLocation()) == 1){

                    Repeater repeater = (Repeater) e.getBlock().getBlockData();



                }
            }

        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(data.getCircuitsLoc().contains(e.getBlock().getLocation())){
            e.setCancelled(true);
        }
    }
}
