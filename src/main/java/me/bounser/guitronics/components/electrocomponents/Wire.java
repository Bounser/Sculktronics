package me.bounser.guitronics.components.electrocomponents;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Bukkit;

import java.awt.*;

public class Wire implements ElectroComponent {

    Circuit circuit;
    int[] pos;

    Color basicColor;
    Color poweredColor;

    boolean powered;

    RectComponent icon;

    public Wire(Circuit circuit, int[] pos){
        basicColor = Data.getInstance().getWireBasicColor();
        poweredColor = Data.getInstance().getWirePoweredColor();

        this.circuit = circuit;
        this.pos = new int[]{pos[0] * 10 + 10, pos[1] * 10 + 10};

        // placeIcon();
    }

    @Override
    public EComponent getEComponent() {
        return EComponent.WIRE;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public void setPowered(boolean setpowered) {
        powered = setpowered;
        // icon.setColor(poweredColor);
    }

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
    public Color getBasicColor() { return basicColor; }

    @Override
    public Color getPoweredColor() {
        return poweredColor;
    }

    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public void placeIcon() {
        /*for(Interaction interaction : circuit.getInteractions()){
            icon = new RectComponent("IconW1", null, false, interaction, pos[0], pos[1], 6, 6, basicColor);
            Bukkit.broadcastMessage("Icon placed");
            interaction.getComponentTree().getComponents().add(icon);
        }*/
    }

    @Override
    public void remove(){
        // icon.dispose();
    }

}
