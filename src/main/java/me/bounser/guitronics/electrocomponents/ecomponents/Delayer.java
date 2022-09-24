package me.bounser.guitronics.electrocomponents.ecomponents;

import me.bounser.guitronics.electrocomponents.EComponent;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Delayer implements EComponent {

    Color basic;
    Color powered;

    public Delayer(){
        int[] bcolor = Data.getInstance().getDelayerBasicColor();
        basic = new Color(bcolor[0], bcolor[1], bcolor[2]);

        int[] pcolor = Data.getInstance().getDelayerPoweredColor();
        powered = new Color(pcolor[0], pcolor[1], pcolor[2]);
    }

    public void changeDelay(){


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
    public String getDirection() {
        return null;
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
