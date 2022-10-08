package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.ElectroComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Inverter implements ElectroComponent {
    @Override
    public EComponent getEComponent() {
        return null;
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
    public void placeIcon(int x, int y, Interaction interaction) {

    }
}
