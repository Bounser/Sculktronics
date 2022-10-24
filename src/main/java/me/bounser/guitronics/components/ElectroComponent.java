package me.bounser.guitronics.components;

import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public interface ElectroComponent {

    EComponent getEComponent();

    boolean isPowered();

    void setPowered(boolean powered);

    int getSecondsDelay();

    boolean isDirectional();

    int getDirection();

    Color getBasicColor();

    Color getPoweredColor();

    boolean hasIcon();

    void placeIcon(int x, int y);

    void remove();

}
