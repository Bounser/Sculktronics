package me.bounser.guitronics.listeners;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RedstoneListener implements Listener {

    Data data = Data.getInstance();

    List<Circuit> activeCooldownCircuits = new ArrayList<>();
    List<Location> circuitBases;

    private static RedstoneListener instance;

    public RedstoneListener(){
        circuitBases = data.getCircuitBases();
        instance = this;
    }

    public static RedstoneListener getInstance(){ return instance; }

    public void addBase(Location loc){ if(!circuitBases.contains(loc)) circuitBases.add(loc); }

    @EventHandler
    public void onRedstoneChange(BlockRedstoneEvent e){

        if(e.getNewCurrent() == 0 || e.getOldCurrent() == 0){

            for(Location circuitBase : circuitBases){

                if(data.getDebug()) Bukkit.broadcastMessage(String.valueOf(e.getBlock().getLocation().distance(circuitBase)));


                if(e.getBlock().getY() == circuitBase.getY() && e.getBlock().getLocation().distance(circuitBase)==1){

                    int offsetZ = 0;
                    int offsetX = 0;
                    
                    if(e.getBlock().getZ() > 0){
                        offsetZ = e.getBlock().getZ() - circuitBase.getBlockZ();
                    } else if(e.getBlock().getZ() < 0){
                        offsetZ = Math.abs(circuitBase.getBlockZ()) - Math.abs(e.getBlock().getZ());
                    }

                    if(e.getBlock().getX() > 0){
                        offsetX = e.getBlock().getX() - circuitBase.getBlockX();
                    } else if(e.getBlock().getX() < 0){
                        offsetX = Math.abs(circuitBase.getBlockX()) - Math.abs(e.getBlock().getX());
                    }

                    if(data.getDebug()) Bukkit.broadcastMessage(offsetZ + " " + offsetX);

                    Circuit cir = CircuitsManager.getInstance().getCircuitFromBaseLocation(circuitBase);

                    List<Integer> outputs = cir.getOutputs();

                    if(data.getDebug()) Bukkit.broadcastMessage("Redstone change NEAR circuit. Outputs: " + outputs);

                    // TO-DO: Make sure the offset works in negative-negative etc coords.

                    if(offsetZ == 1){

                        if(!outputs.contains(37)){
                            if(data.getDebug()) Bukkit.broadcastMessage("input change: " + cir);
                            if (e.getNewCurrent() != 0) {
                                cir.addInput(37);
                            } else {
                                cir.removeInput(37);
                            }
                        }
                    }
                    if(offsetZ == -1){

                        if(!outputs.contains(45)) {
                            if (data.getDebug()) Bukkit.broadcastMessage("input change: " + cir);
                            if (e.getNewCurrent() != 0) {
                                cir.addInput(45);
                            } else {
                                cir.removeInput(45);
                            }
                        }
                    }

                    if(offsetX == -1){

                        if(!outputs.contains(5)) {
                            if (data.getDebug()) Bukkit.broadcastMessage("input change: " + cir);
                            if (e.getNewCurrent() != 0) {
                                cir.addInput(5);
                            } else {
                                cir.removeInput(5);
                            }
                        }
                    }
                    if(offsetX == 1){

                        if(!outputs.contains(77)) {
                            if (data.getDebug()) Bukkit.broadcastMessage("input change: " + cir);
                            if (e.getNewCurrent() != 0) {
                                cir.addInput(77);
                            } else {
                                cir.removeInput(77);
                            }
                        }
                    }
                    if(data.getDebug()) Bukkit.broadcastMessage("inputs: " + cir.getInputs());

                    if(data.getClockPrevention()){

                        if(activeCooldownCircuits.contains(cir)) {
                            cir.setOverloaded();
                            return;
                        }

                        activeCooldownCircuits.add(cir);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                activeCooldownCircuits.remove(cir);
                            }
                        }.runTaskLater(GUItronics.getInstance(), data.getTicksPerChange());
                    }
                }
            }
        }
    }

}
