package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;

public class Diode implements ElectroComponent {

    int direction;

    Circuit circuit;

    Color basic;
    Color powered;

    public Diode(Circuit circuit, int direction){
        this.direction = direction;

        basic = Data.getInstance().getDiodeBasicColor();
        powered = Data.getInstance().getDiodePoweredColor();

        this.circuit = circuit;
    }

    public void rotate(){

        if (direction == 0){
            direction = 1;
        } else if (direction == 1){
            direction = 2;
        } else if (direction == 2){
            direction = 3;
        } else if (direction == 3){
            direction = 0;
        }

    }

    @Override
    public EComponent getEComponent() {
        return EComponent.DIODE;
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
    public int getDirection() {
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
