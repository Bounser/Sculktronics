package me.bounser.guitronics.tools;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.EComponent;
import org.bukkit.Location;

public class Miscellaneous {


    private static Miscellaneous instance;

    public static Miscellaneous getInstance(){
        return instance == null ? instance = new Miscellaneous() : instance;
    }

    public int getRoundFromEComponent(EComponent EComponent){

        switch(EComponent){
            case WIRE: return 0;
            case DELAYER: return 2;
            case DIODE: return 2;
            case RESISTOR: return 3;
        }
        return 0;
    }

    public int[] getNumberOfPixels(Circuit circuit){

        int size = circuit.getLocation().size();

        switch (size){

            case 1:
                int[] i = new int[2];
                i[0] = 9;
                i[1] = 9;
                return i;
            case 2:
                // CHECK ORIENTATION ¿?
                int[] j = new int[2];
                j[0] = 9;
                j[1] = 9;
                return j;
            case 4:
                int[] k = new int[2];
                k[0] = 22;
                k[1] = 22;
                return k;

        }
        return null;
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
