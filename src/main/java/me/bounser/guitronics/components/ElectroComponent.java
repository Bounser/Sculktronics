package me.bounser.guitronics.components;

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

    void placeIcon();

    void remove();

}
