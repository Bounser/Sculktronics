package me.bounser.guitronics.circuits;

import me.bounser.guitronics.electrocomponents.EComponent;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.electrocomponents.ecomponents.Delayer;
import me.bounser.guitronics.electrocomponents.ecomponents.Diode;
import me.bounser.guitronics.electrocomponents.ecomponents.Resistor;
import me.bounser.guitronics.electrocomponents.ecomponents.Wire;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Circuit {

    // Persistent info:
    // Locations of the different bases. Possible combinations: 1 | 1 2 | 1 2 3 4
    HashMap<Integer, Location> locations;
    // UUID of the owner.
    String owneruuid;
    // This is the design of the circuit. Here is stored all the information related to all things the player modified.
    HashMap<Integer, Object> design;

    // All inputs/outputs. Correspond to physical blocks and pixels within the circuit. More info in Data.java
    List<Integer> inputs = new ArrayList<>();
    List<Integer> outputs = new ArrayList<>();
    // Temp info.
    // List of wires that will be rendered in the next visual render.
    List<Integer> toRender = new ArrayList<>();
    // All interactions involved with this circuit. All changes will be done to ALL current interactions.
    List<Interaction> interactions = new ArrayList<>();

    // State of the circuit.
    Boolean overloaded = false;

    public Circuit(HashMap<Integer, Location> loc, String uuid, HashMap<Integer, Object> design, List<Integer> inputs, List<Integer> outputs){
        locations = loc;
        owneruuid = uuid;
        if(design == null){
            this.design = new HashMap<Integer, Object>();
        } else {
            this.design = design;
        }
    }

    // Getters
    public HashMap<Integer, Location> getLocation(){ return locations; }

    public String getOwneruuid(){ return owneruuid; }

    public HashMap<Integer, Object> getDesign(){ return design; }

    public List<Integer> getInputs(){ return inputs; }

    public List<Integer> getOutputs(){ return outputs; }

    public List<Integer> getRender(){ return toRender; }

    public Color getColor(Object pos, boolean powered){

        switch(getElectroComponent(design.get(pos))){
            case WIRE:
                Wire wire = (Wire) design.get(pos);
                if(powered) return wire.getPoweredColor();
                return wire.getBasicColor();
            case DELAYER:
                Delayer delayer = (Delayer) design.get(pos);
                if(powered) return delayer.getPoweredColor();
                return delayer.getBasicColor();
            case DIODE:
                Diode diode = (Diode) design.get(pos);
                if(powered) return diode.getPoweredColor();
                return diode.getBasicColor();
        }
        return null;
    }

    public ElectroComponent getElectroComponent(Object EComponent){

        if(EComponent instanceof Wire){
            return ((Wire) EComponent).getEComponent();
        }
        if(EComponent instanceof Delayer){
            return ((Delayer) EComponent).getEComponent();
        }
        if(EComponent instanceof Diode){
            return ((Diode) EComponent).getEComponent();
        }
        return null;
    }

    public boolean getPoweredState(Object pos){

        switch(getElectroComponent(design.get(pos))){
            case WIRE:
                Wire wire = (Wire) design.get(pos);
                return wire.isPowered();
            case DELAYER:
                Delayer delayer = (Delayer) design.get(pos);
                return delayer.isPowered();
            case DIODE:
                Diode diode = (Diode) design.get(pos);
                return diode.isPowered();
        }
        return false;
    }

    public EComponent getEComponent(int pos){ return (EComponent) design.get(pos); }

    // adders / removers

    public void addEComponent(int pos, Object eComponent){ design.put(pos, eComponent); }

    public void removeEComponent(Object pos){ if(design.containsKey(pos)) design.remove(pos); }

    public void addToRender(int powered){ toRender.add(powered); }

    public void addInput(int input){ if(!inputs.contains(input)) inputs.add(input); updateRender(true); }

    public void removeInput(Object input){ inputs.remove(input); updateRender(true); }

    public void addOutput(int output){ outputs.add(output);}

    public void updateRender(boolean newRender){

        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Tries to render.");
        if(overloaded) return;

        if(newRender){
            outputs.clear();
            toRender.clear();
            CircuitRenderer.getInstance().render(this);
            CircuitRenderer.getInstance().outputRedstone(getOutputs(), this);
        }
        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Renders:");
        setDesign(false);

        for(Interaction i : interactions){
            if(i != null){
                for(int j : toRender){

                    updateRect(design.get(j), i, j, true);

                }

                for(int k : Arrays.asList(5, 37, 45, 77)){

                    i.getComponentTree().locate(k + "t").setHidden(!inputs.contains(k));
                    i.getComponentTree().locate(k + "f").setHidden(!outputs.contains(k));
                }
            }
        }
    }

    public void setDesign(boolean updateRender){

        for(Interaction i : interactions) {
            if (i != null) {
                for (int j : design.keySet()) {

                    updateRect(design.get(j), i, j, false);

                }
            }
            if(updateRender) {
                updateRender(true);
            }
        }
    }

    public void updateRect(Object electroComponent, Interaction i, int id, Boolean powered){

        switch(getElectroComponent(electroComponent)){

            case WIRE: Wire wire = (Wire) electroComponent;
                RectComponent blockw = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockw.setColor(wire.getPoweredColor());
                else blockw.setColor(wire.getBasicColor()); break;

            case DELAYER: Delayer delayer = (Delayer) electroComponent;
                RectComponent blockde = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockde.setColor(delayer.getPoweredColor());
                else blockde.setColor(delayer.getBasicColor()); break;

            case DIODE: Diode diode = (Diode) electroComponent;
                RectComponent blockdi = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockdi.setColor(diode.getPoweredColor());
                else blockdi.setColor(diode.getBasicColor()); break;

            case RESISTOR: Resistor resistor = (Resistor) electroComponent;
                RectComponent blockr = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockr.setColor(resistor.getPoweredColor());
                else blockr.setColor(resistor.getBasicColor()); break;

        }
    }

    public void addInteraction(Interaction interaction) { if(!interactions.contains(interaction)) interactions.add(interaction); }

    public void removeInteraction(Interaction interaction) { interactions.remove(interaction); }

    public void changeSize(List<Location> LocToAdd){

        for(Location loc : LocToAdd){

            if(locations.size() == 1 && LocToAdd.size() == 1){
                locations.put(2, LocToAdd.get(0));
            }
            if(locations.size() == 2 && LocToAdd.size() == 2){
                locations.put(3, LocToAdd.get(0));
                locations.put(4, LocToAdd.get(1));
            }
        }

    }

    public void setOverloaded(){ overloaded = true; }

    public void unsetOverloaded(){ overloaded = false; }

}
