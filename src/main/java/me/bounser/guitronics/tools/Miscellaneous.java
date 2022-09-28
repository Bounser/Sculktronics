package me.bounser.guitronics.tools;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import org.bukkit.Location;

public class Miscellaneous {


    private static Miscellaneous instance;

    public static Miscellaneous getInstance(){
        return instance == null ? instance = new Miscellaneous() : instance;
    }

    public int getRoundFromElectroComponent(ElectroComponent electroComponent){

        switch(electroComponent){
            case WIRE: return 0;
            case DELAYER: return 2;
            case DIODE: return 2;
            case RESISTOR: return 3;
        }
        return 0;
    }

    public int[] getNumberOfPixels(Circuit circuit){

        circuit.get
    }

    public char getOffset(Location loc1, Location loc2){

        if(loc1.getX() == loc2.getX()){

            if(loc1.getZ() > loc2.getZ()){
                return 'S';
            } else {
                return 'N';
            }
        }
        if(loc1.getZ() == loc2.getZ()){
            if(loc1.getX() > loc2.getX()){
                return 'E';
            } else {
                return 'W';
            }
        }

        return 'A';
    }







}
