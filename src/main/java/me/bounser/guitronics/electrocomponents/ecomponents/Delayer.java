package me.bounser.guitronics.electrocomponents.ecomponents;

import me.bounser.guitronics.electrocomponents.EComponent;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Delayer implements EComponent {

    // Delay of the delayer in ticks.
    int delay;

    Color basic;
    Color powered;

    public Delayer(int delay){
        basic = Data.getInstance().getDelayerBasicColor();
        powered = Data.getInstance().getDelayerPoweredColor();

        this.delay = delay;
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

    @Override
    public ElectroComponent getEComponent() {
        return ElectroComponent.DELAYER;
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
    public char getDirection() {
        return 'X';
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
