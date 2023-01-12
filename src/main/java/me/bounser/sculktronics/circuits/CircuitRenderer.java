package me.bounser.sculktronics.circuits;

import me.bounser.sculktronics.components.EComponent;
import me.bounser.sculktronics.components.electrocomponents.Diode;
import me.bounser.sculktronics.components.electrocomponents.Wire;
import org.bukkit.Location;
import org.bukkit.block.data.Powerable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CircuitRenderer {

    // Utilities to render the proper circuit reactions depending on the corresponding design.

    private static CircuitRenderer  instance;
    public static CircuitRenderer  getInstance(){
        return instance == null ? instance = new CircuitRenderer() : instance;
    }

    public void render(Circuit cir){

        HashMap<Integer, Object> design = cir.getDesign();

        for(int signalsIn : cir.getSignalsIn()){

            if(cir.getDesign().get(signalsIn) instanceof Wire){
                cir.addToRender(signalsIn);
                checkPixel(signalsIn, design, cir);
            }
        }
    }

    public void checkPixel(int i, HashMap<Integer, Object> design, Circuit cir){

        List<Integer> nearPoints = new ArrayList<>();

        switch(cir.getSize()){
            case 0:
            case 2:
                nearPoints.add(9);
                nearPoints.add(-9);
                break;
            case 1:
            case 3:
                nearPoints.add(21);
                nearPoints.add(-21);
        }
        nearPoints.add(1);
        nearPoints.add(-1);

        for(int j : nearPoints){

            int x = j+i;

            if(!cir.getRender().contains(x)) {

                if (design.containsKey(x)) {

                    if(cir.getEComponent(cir.getElectroComponent(x)).equals(EComponent.DIODE)){

                        // DIODE LOGIC - GET OFFSET OF INTEGERS (CARDINAL)

                        Diode diode = (Diode) cir.getElectroComponent(x);
                        if(diode.getDirection() == 0 && j < -1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 1 && j == -1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 2 && j < 1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 3 && j == 1){
                            checkPixel(j*2 +i, design, cir);
                        }

                    } else if (cir.getEComponent(cir.getElectroComponent(x)).equals(EComponent.NOT)) {

                        // INVERTER LOGIC - GET OFFSET OF INTEGERS (CARDINAL)

                        Diode diode = (Diode) cir.getElectroComponent(x);
                        if(diode.getDirection() == 0 && j < -1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 1 && j == -1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 2 && j < 1){
                            checkPixel(j*2 +i, design, cir);
                        }
                        if(diode.getDirection() == 3 && j == 1){
                            checkPixel(j*2 +i, design, cir);
                        }

                    }else {
                            if (x == 5 || x == 37 || x == 45 || x == 77) {
                                // Adds output
                                cir.modifySignalsOut(false, x);
                            }

                            cir.addToRender(j + i);
                            checkPixel(j + i, design, cir);
                    }
                }
            }
        }
    }

    public void outputRedstone(Circuit cir){

        for(int output : cir.getSignalsOut()){

            Location cb = cir.getLocation();
            Location base = new Location(cb.getWorld(), cb.getX(), cb.getY(), cb.getZ());

            switch(output){
                case 5:  base.add(0,-1,-1); break;
                case 37: base.add(-1,-1,0); break;
                case 45: base.add(1,-1,0); break;
                case 77: base.add(0,-1,1); break;
            }

            if(base.getBlock().getBlockPower() == 0){
                Powerable rw = (Powerable) base.getBlock().getBlockData();
                rw.setPowered(true);
                base.getBlock().setBlockData(rw);
            }
        }
    }
}
