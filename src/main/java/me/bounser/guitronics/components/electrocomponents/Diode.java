package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;

import java.awt.*;
import java.util.List;

public class Diode implements ElectroComponent {

    int direction;

    Circuit circuit;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    List<RectComponent> icon;

    public Diode(Circuit circuit, int[] pos, int direction){
        this.direction = direction;

        basicColor = Data.getInstance().getDiodeBasicColor();
        poweredColor = Data.getInstance().getDiodePoweredColor();

        this.circuit = circuit;

        placeIcon(pos[0], pos[1]);
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
    public void setPowered(boolean setpowered) { powered = setpowered; }

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
    public Color getBasicColor() { return basicColor; }

    @Override
    public Color getPoweredColor() { return poweredColor; }

    @Override
    public boolean hasIcon() {
        return true;
    }

    @Override
    public void placeIcon(int x, int y) {

        Color black = new Color(0,0,0);

        for(Interaction interaction : circuit.getInteractions())
        switch(direction){

            case 0:
                icon.add(new RectComponent("IconD1", null, false, interaction, x + 4, y + 1, 2, 8, black));
                icon.add(new RectComponent("IconD2", null, false, interaction, x + 2, y + 3, 6, 2, black));
                icon.add(new RectComponent("IconD3", null, false, interaction, x + 3, y + 2, 4, 2, black));
                break;
            case 1:
                icon.add(new RectComponent("IconD1", null, false, interaction, x + 1, y + 4, 8, 2, black));
                icon.add(new RectComponent("IconD2", null, false, interaction, x + 5, y + 2, 2, 6, black));
                icon.add(new RectComponent("IconD3", null, false, interaction, x + 6, y + 3, 2, 4, black));
                break;
            case 2:
                icon.add(new RectComponent("IconD1", null, false, interaction, x + 4, y + 1, 2, 8, black));
                icon.add(new RectComponent("IconD2", null, false, interaction, x + 2, y + 5, 6, 2, black));
                icon.add(new RectComponent("IconD3", null, false, interaction, x + 3, y + 6, 4, 2, black));
                break;
            case 3:
                icon.add(new RectComponent("IconD1", null, false, interaction, x + 1, y + 4, 8, 2, black));
                icon.add(new RectComponent("IconD2", null, false, interaction, x + 3, y + 2, 2, 6, black));
                icon.add(new RectComponent("IconD3", null, false, interaction, x + 2, y + 3, 2, 4, black));
                break;

        }
    }

    @Override
    public void remove() {
        for(RectComponent rect : icon){
            rect.dispose();
        }
    }
}
