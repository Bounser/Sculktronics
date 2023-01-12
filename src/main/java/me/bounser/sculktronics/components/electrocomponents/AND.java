package me.bounser.sculktronics.components.electrocomponents;

import me.bounser.sculktronics.circuits.Circuit;
import me.bounser.sculktronics.components.EComponent;
import me.bounser.sculktronics.components.ElectroComponent;
import me.bounser.sculktronics.tools.Data;
import me.leoko.advancedgui.manager.ResourceManager;
import me.leoko.advancedgui.utils.components.ImageComponent;

import java.awt.*;

public class AND implements ElectroComponent {

    boolean negated;

   /*
    TRUTH TABLE:

    A B | Output
    0 0 | 0
    0 1 | 0
    1 0 | 0
    1 1 | 1

    */

    Circuit circuit;
    int[] pos;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    public AND(Circuit circuit, int[] pos, int direction, boolean negated){
        basicColor = Data.getInstance().getWireBasicColor();
        poweredColor = Data.getInstance().getWirePoweredColor();
        powered = false;

        this.circuit = circuit;
        this.pos = new int[]{pos[0], pos[1]};
        this.negated = negated;
    }

    @Override
    public EComponent getEComponent() {
        return null;
    }

    @Override
    public int getLocations() {
        return 0;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public int getOutput() {
        return 0;
    }

    @Override
    public int getDirection() {
        return 0;
    }

    @Override
    public Color getBasicColor() {
        return null;
    }

    @Override
    public Color getPoweredColor() {
        return null;
    }

    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public void placeIcon() {

        ImageComponent and = new ImageComponent("and", null, false,null, pos[0],pos[1], ResourceManager.getInstance().getImage("AND"));

    }

    @Override
    public void removeIcon() {

    }
}
