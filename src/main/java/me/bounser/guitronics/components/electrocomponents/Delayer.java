package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;

import java.awt.*;

public class Delayer implements ElectroComponent {

    Circuit circuit;
    int pos;
    // Delay of the delayer in ticks.
    int delay;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    public Delayer(Circuit circuit, int[] pos, int delay){
        basicColor = Data.getInstance().getDelayerBasicColor();
        poweredColor = Data.getInstance().getDelayerPoweredColor();

        this.delay = delay;
        this.circuit = circuit;

    }

    public void changeDelay(){

        if(delay == 6){
            delay = 4;
        } else if(delay == 4){
            delay = 2;
        } else if(delay == 2){
            delay = 6;
        }

    }

    public int getDelay(){
        return delay;
    }

    @Override
    public EComponent getEComponent() {
        return EComponent.DELAYER;
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    @Override
    public void setPowered(boolean setpowered) { powered = setpowered; }

    @Override
    public int getSecondsDelay() {
        return 1;
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

    @Override
    public int getDirection() {
        return -1;
    }

    @Override
    public Color getBasicColor() {
        return basicColor;
    }

    @Override
    public Color getPoweredColor() {
        return poweredColor;
    }

    @Override
    public boolean hasIcon() {
        return true;
    }

    @Override
    public void placeIcon() {





    }

    @Override
    public void remove() {

    }
}
