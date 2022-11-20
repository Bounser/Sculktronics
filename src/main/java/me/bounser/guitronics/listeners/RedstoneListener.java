package me.bounser.guitronics.listeners;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RedstoneListener implements Listener {

    Data data = Data.getInstance();

    List<Circuit> activeCooldownCircuits = new ArrayList<>();

    private static RedstoneListener instance;

    public RedstoneListener(){
        instance = this;
    }

    public static RedstoneListener getInstance(){ return instance; }

    @EventHandler
    public void onRedstoneChange(BlockRedstoneEvent e){

        if((e.getNewCurrent() == 0 || e.getOldCurrent() == 0) && e.getBlock().getBlockData().getMaterial().equals(Material.REPEATER)){

            Circuit cir = null;
            for(Circuit circuit : CircuitsManager.getInstance().getAllCircuits()) {

                if(circuit.getPutsLocations().containsKey(e.getBlock().getLocation())){

                    cir = circuit;

                }
            }

            if(data.getClockPrevention() && cir != null){

                if(activeCooldownCircuits.contains(cir)) {
                    cir.setOverloaded();
                    return;
                }

                activeCooldownCircuits.add(cir);

                Circuit finalCir = cir;

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        activeCooldownCircuits.remove(finalCir);
                        finalCir.unsetOverloaded();

                    }
                }.runTaskLater(GUItronics.getInstance(), data.getTicksPerChange());
            }
        }
    }
}
