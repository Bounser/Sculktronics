package me.bounser.guitronics.components;

import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public interface ElectroComponent {

    EComponent getEComponent();

    boolean isPowered();

    int getSecondsDelay();

    boolean isDirectional();

    int getDirection();

    Color getBasicColor();

    Color getPoweredColor();

    boolean hasIcon();

    void placeIcon(int x, int y, Interaction interaction);

}
