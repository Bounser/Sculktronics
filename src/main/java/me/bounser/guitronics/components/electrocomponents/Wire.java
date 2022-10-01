package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Wire implements ElectroComponent {

    Circuit circuit;

    Color basic;
    Color powered;

    public Wire(Circuit circuit){
        basic = Data.getInstance().getWireBasicColor();
        powered = Data.getInstance().getWirePoweredColor();

        this.circuit = circuit;
    }

    @Override
    public EComponent getEComponent() {
        return EComponent.WIRE;
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
    public int getDirection() {
        return -1;
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
