package me.bounser.guitronics.electrocomponents;

import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public interface EComponent {

    ElectroComponent getEComponent();

    boolean isPowered();

    int getSecondsDelay();

    boolean isDirectional();

    char getDirection();

    Color getBasicColor();

    Color getPoweredColor();

    boolean hasIcon();

    void placeIcon(int x, int y, Interaction interaction);

}
