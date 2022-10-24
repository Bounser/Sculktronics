package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;
import java.util.List;

public class Resistor implements ElectroComponent {

    Circuit circuit;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    List<RectComponent> icon;

    public Resistor(Circuit circuit, int[] pos){

        basicColor = Data.getInstance().getDiodeBasicColor();
        poweredColor = Data.getInstance().getDiodePoweredColor();

        this.circuit = circuit;
        placeIcon(pos[0], pos[1]);
    }

    @Override
    public EComponent getEComponent() {
        return EComponent.RESISTOR;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public void setPowered(boolean setpowered) { powered = setpowered; }

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
        return -1;
    }

    @Override
    public Color getBasicColor() {
        return basicColor;
    }

    @Override
    public Color getPoweredColor() {
        return poweredColor;
    }

    @Override
    public boolean hasIcon() {
        return true;
    }

    @Override
    public void placeIcon(int x, int y) {

        Color black = new Color(0,0,0);

        for(Interaction interaction : circuit.getInteractions()){
            icon.add(new RectComponent("IconR1", null, false, interaction, x + 2, y + 2, 6, 1, black));
            icon.add(new RectComponent("IconR2", null, false, interaction, x + 7, y + 2, 1, 6, black));
            icon.add(new RectComponent("IconR3", null, false, interaction, x + 3, y + 7, 5, 1, black));
            icon.add(new RectComponent("IconR4", null, false, interaction, x + 2, y + 2, 1, 6, black));
        }
    }

    @Override
    public void remove() {
        for(RectComponent rect : icon){
            rect.dispose();
        }
    }
}
