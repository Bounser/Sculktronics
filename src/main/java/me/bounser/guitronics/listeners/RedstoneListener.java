package me.bounser.guitronics.listeners;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.tools.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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

            for(Circuit circuit : CircuitsManager.getInstance().getAllCircuits()) {

                if(circuit.getPutsLocations().containsKey(e.getBlock().getLocation())){

                    Location loc = e.getBlock().getLocation();

                    if(circuit.getInputs().contains(circuit.getPutsLocations().get(e.getBlock().getLocation()))){

                        if(e.getNewCurrent() > 0){
                            int point = 0;
                            switch(circuit.getPutsLocations().get(loc)){
                                case 0:
                                    point = 5; break;
                                case 1:
                                    point = 45; break;
                                case 2:
                                    point = 77; break;
                                case 3:
                                    point = 37; break;
                            }
                            circuit.modifySignalsIn(false, point);
                        }else{
                            int point = 0;
                            switch(circuit.getPutsLocations().get(loc)){
                                case 0:
                                    point = 5; break;
                                case 1:
                                    point = 45; break;
                                case 2:
                                    point = 77; break;
                                case 3:
                                    point = 37; break;
                            }
                            circuit.modifySignalsIn(true, point);
                        }
                        circuit.updateRender(true);
                    } else if(circuit.getOutputs().contains(circuit.getPutsLocations().get(loc))){
                        if(!circuit.getSignalsOut().contains(circuit.getPutsLocations().get(loc))){
                            e.setNewCurrent(0);
                        }
                    }
                }
                if(data.getClockPrevention()){

                    if(circuit.getOverloaded()) return;

                    if(activeCooldownCircuits.contains(circuit)) {
                        circuit.setOverloaded();

                        // Animation

                        // circuit.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, circuit.getLocation(),30, 1, 0,1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                circuit.unsetOverloaded();

                            }
                        }.runTaskLater(GUItronics.getInstance(), 50);
                        return;
                    }

                    activeCooldownCircuits.add(circuit);

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            activeCooldownCircuits.remove(circuit);

                        }
                    }.runTaskLater(GUItronics.getInstance(), data.getTicksPerChange());
                }
            }
        }
    }
}
