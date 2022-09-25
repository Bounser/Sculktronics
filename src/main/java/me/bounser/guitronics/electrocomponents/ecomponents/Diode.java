package me.bounser.guitronics.electrocomponents.ecomponents;

import me.bounser.guitronics.electrocomponents.EComponent;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Diode implements EComponent {

    char direction;

    Color basic;
    Color powered;

    public Diode(char direction){
        this.direction = direction;

        basic = Data.getInstance().getDiodeBasicColor();
        powered = Data.getInstance().getDiodePoweredColor();
    }

    public void rotate(){

        if (direction == 'N'){
            direction = 'E';
        } else if (direction == 'E'){
            direction = 'S';
        } else if (direction == 'S'){
            direction = 'W';
        } else if (direction == 'W'){
            direction = 'N';
        }

    }

    @Override
    public ElectroComponent getEComponent() {
        return ElectroComponent.DIODE;
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
        return true;
    }

    @Override
    public char getDirection() {
        return direction;
    }

    @Override
    public Color getBasicColor() { return basic; }

    @Override
    public Color getPoweredColor() { return powered; }

    @Override
    public boolean hasIcon() {
        return true;
    }

    @Override
    public void placeIcon(int x, int y, Interaction interaction) {

    }
}
