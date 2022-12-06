package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Wire implements ElectroComponent {

    Circuit circuit;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    RectComponent icon;

    public Wire(Circuit circuit){
        basicColor = Data.getInstance().getWireBasicColor();
        poweredColor = Data.getInstance().getWirePoweredColor();

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
    public void setPowered(boolean setpowered) {
        powered = setpowered;
        icon.setColor(poweredColor);
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
    public void placeIcon(int x, int y) {
        for(Interaction interaction : circuit.getInteractions()){
                icon = new RectComponent("IconW1", null, false, interaction, x + 2, y + 2, 1, 6, basicColor);
        }
    }

    @Override
    public void remove(){
        icon.dispose();
    }

}
