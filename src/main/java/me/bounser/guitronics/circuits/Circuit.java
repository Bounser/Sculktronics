package me.bounser.guitronics.circuits;

import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.electrocomponents.Delayer;
import me.bounser.guitronics.components.electrocomponents.Diode;
import me.bounser.guitronics.components.electrocomponents.Resistor;
import me.bounser.guitronics.components.electrocomponents.Wire;
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
    // Location of the main base, located at the north-west.
    Location location;
    /* Sizes of the circuit. 0 = 1x1 1 = 1x2 2 = 2x1 3 = 2x2
    *                                       N
    *      Size 2: oo  Size 3:  o         W X E
    *                           o           S
    */
    int size;
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

    public Circuit(Location loc, int size, String uuid, HashMap<Integer, Object> design, List<Integer> inputs, List<Integer> outputs){
        location = loc;
        owneruuid = uuid;
        this.size = size;
        if(design == null){
            this.design = new HashMap<Integer, Object>();
        } else {
            this.design = design;
        }
    }

    // Getters
    public Location getLocation(){ return location; }

    public int getSize(){ return size; }

    public String getOwneruuid(){ return owneruuid; }

    public HashMap<Integer, Object> getDesign(){ return design; }

    public List<Integer> getInputs(){ return inputs; }

    public List<Integer> getOutputs(){ return outputs; }

    public List<Integer> getRender(){ return toRender; }

    public Color getColor(Object pos, boolean powered){

        switch(getEComponent(design.get(pos))){
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

    public EComponent getEComponent(Object EComponent){

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

        switch(getEComponent(design.get(pos))){
            case WIRE:
                Wire wire = (Wire) design.get(pos);
                return wire.isPowered();
            case DELAYER:
                Delayer delayer = (Delayer) design.get(pos);
                return delayer.isPowered();
            case DIODE:
                Diode diode = (Diode) design.get(pos);
                return diode.isPowered();
            case RESISTOR:
                Resistor resistor = (Resistor) design.get(pos);
                return resistor.isPowered();
         }
        return false;
    }

    public ElectroComponent getElectroComponent(int pos){ return (ElectroComponent) design.get(pos); }

    // adders / removers

    public void addElectroComponent(int pos, Object ElectroComponent){ design.put(pos, ElectroComponent); }

    public void removeEComponent(Object pos){ if(design.containsKey(pos)) design.remove(pos); }

    public void addToRender(int powered){ toRender.add(powered); }

    public void addInput(int input){
        if(!inputs.contains(input)) inputs.add(input);
        CircuitRenderer.getInstance().renderPuts(this);
    }

    public void removeInput(Object input){ inputs.remove(input); updateRender(true); }

    public void addOutput(int output){
        if(!outputs.contains(output)) outputs.add(output);
        CircuitRenderer.getInstance().renderPuts(this);
    }

    public void removeOutput(Object output){ inputs.remove(output); updateRender(true); }

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

        switch(getEComponent(electroComponent)){

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

    public void addLocation(Location location){

        if(locations.size() < 5){
            locations.put(locations.size() + 1, location);
        }

    }

    // Direction: INT corresponding to clockwise change.

    public void expand(int direction){
        switch (direction){
            case 0:
                location = location.add(1,0,0); break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                location = location.add(0,0,1); break;

        }

    }

    public List<Interaction> getInteractions(){ return interactions; }

    public void setOverloaded(){ overloaded = true; }

    public void unsetOverloaded(){ overloaded = false; }

}
