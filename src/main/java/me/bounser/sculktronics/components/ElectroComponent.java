package me.bounser.sculktronics.components;

import java.awt.*;

public interface ElectroComponent {

    EComponent getEComponent();

    int getLocations();

    boolean isPowered();

    int getOutput();

    int getDirection();

    Color getBasicColor();

    Color getPoweredColor();

    boolean hasIcon();

    void placeIcon();

    void removeIcon();

}
