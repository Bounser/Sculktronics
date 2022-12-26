package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;

import java.awt.*;

public class Wire implements ElectroComponent {

    Circuit circuit;
    int[] pos;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    public Wire(Circuit circuit, int[] pos){
        basicColor = Data.getInstance().getWireBasicColor();
        poweredColor = Data.getInstance().getWirePoweredColor();
        powered = false;

        this.circuit = circuit;
        this.pos = new int[]{pos[0], pos[1]};
    }

    @Override
    public EComponent getEComponent() {
        return EComponent.WIRE;
    }

    @Override
    public int getLocations() {
        return 0;
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    @Override
    public int getOutput() {
        return 0;
    }

    @Override
    public int getDirection() {
        return -1;
    }

    @Override
    public Color getBasicColor() { return basicColor; }

    @Override
    public Color getPoweredColor() {
        return poweredColor;
    }

    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public void placeIcon() {
    }

    @Override
    public void removeIcon(){
    }

}
