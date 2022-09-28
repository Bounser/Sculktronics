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
