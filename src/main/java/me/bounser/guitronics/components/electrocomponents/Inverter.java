package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;
import java.util.List;

public class Inverter implements ElectroComponent {

    Circuit circuit;
    int[] pos;
    int direction;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    List<RectComponent> icon;

    public Inverter(Circuit circuit, int[] pos, int direction){
        this.direction = direction;

        basicColor = Data.getInstance().getDiodeBasicColor();
        poweredColor = Data.getInstance().getDiodePoweredColor();

        this.circuit = circuit;
        this.pos = new int[]{pos[0] * 10 + 10, pos[1] * 10 + 10};
        placeIcon();
    }

    @Override
    public EComponent getEComponent() {
        return null;
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
        Color black = new Color(0,0,0);

        for(Interaction interaction : circuit.getInteractions()){
            icon.add(new RectComponent("IconI", null, false, interaction, pos[0] + 2, pos[1] + 5, 2, 4, black));
            icon.add(new RectComponent("IconI", null, false, interaction, pos[0] + 2, pos[1] + 4, 5, 2, black));
            icon.add(new RectComponent("IconI", null, false, interaction, pos[0] + 6, pos[1] + 1, 2, 5, black));
        }

    }

    @Override
    public void remove() {
        for(RectComponent rect : icon){
            rect.dispose();
        }
    }
}
