package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.List;

public class Diode implements ElectroComponent {

    int direction;

    Circuit circuit;

    Color basic;
    Color powered;

    List<RectComponent> icon;

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

        Color black = new Color(0,0,0);

        icon.add(new RectComponent("IconI", null, false, interaction, x + 2, y + 5, 2, 4, black));
        icon.add(new RectComponent("IconI", null, false, interaction, x + 2, y + 4, 5, 2, black));
        icon.add(new RectComponent("IconI", null, false, interaction, x + 6, y + 1, 2, 5, black));

    }

    @Override
    public void remove() {

        for(RectComponent rect : icon){
            rect.dispose();
        }

    }
}
