package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.ElectroComponent;

import java.awt.*;

public class Resistor implements ElectroComponent {

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

    }

    @Override
    public void removeIcon() {

    }
}
