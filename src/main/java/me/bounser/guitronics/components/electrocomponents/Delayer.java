package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Delayer implements ElectroComponent {

    // Delay of the delayer in ticks.
    int delay;

    Circuit circuit;

    Color basic;
    Color powered;

    public Delayer(Circuit circuit, int delay){
        basic = Data.getInstance().getDelayerBasicColor();
        powered = Data.getInstance().getDelayerPoweredColor();

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
        return false;
    }

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
        return basic;
    }

    @Override
    public Color getPoweredColor() {
        return powered;
    }

    @Override
    public boolean hasIcon() {
        return true;
    }

    @Override
    public void placeIcon(int x, int y, Interaction interaction) {

    }
}
