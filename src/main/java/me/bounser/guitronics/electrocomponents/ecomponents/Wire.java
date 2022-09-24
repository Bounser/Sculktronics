package me.bounser.guitronics.electrocomponents.ecomponents;

import me.bounser.guitronics.electrocomponents.EComponent;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Wire implements EComponent {

    Color basic;
    Color powered;

    public Wire(){
        basic = Data.getInstance().getWireBasicColor();
        powered = Data.getInstance().getWirePoweredColor();
    }

    @Override
    public ElectroComponent getEComponent() {
        return ElectroComponent.WIRE;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public int getSecondsDelay() {
        return 0;
    }

    @Override
    public boolean isDirectional() {
        return false;
    }

    @Override
    public char getDirection() {
        return 'X';
    }

    @Override
    public Color getBasicColor() { return basic; }

    @Override
    public Color getPoweredColor() {
        return powered;
    }

    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public void placeIcon(int x, int y, Interaction interaction) {

    }

}
