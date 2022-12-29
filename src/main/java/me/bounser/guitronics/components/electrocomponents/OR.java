package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.ElectroComponent;

import java.awt.*;

public class OR implements ElectroComponent {

    boolean negated;

    /*
    TRUTH TABLE:

    A B | Output
    0 0 | 0
    0 1 | 1
    1 0 | 1
    1 1 | 1

     */

    public OR(boolean negated){ this.negated = negated; }

    @Override
    public EComponent getEComponent() {
        return EComponent.OR;
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

    }

    @Override
    public void removeIcon() {

    }
}
