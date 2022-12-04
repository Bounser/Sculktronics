package me.bounser.guitronics.circuits;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.advancedgui.AGUIInstances;
import me.bounser.guitronics.components.ElectroComponent;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.electrocomponents.*;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.manager.GuiWallManager;
import me.leoko.advancedgui.utils.GuiWallInstance;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Circuit {


    // Persistent info:
    // Location of the main base, located at the north-west block within the cicuit base.
    final Location LOC;
    
    /** Sizes of the circuit. 0 = 1x1 1 = 1x2 2 = 2x1 3 = 2x2
     *                                       N
     *      Size 1: oo  Size 2:  o         W X E
     *                           o           S
     */
    int size;

    int num;
    // UUID of the owner.
    String owneruuid;
    // This is the design of the circuit. Here is stored all the information related to all things the player modified.
    HashMap<Integer, Object> design;

    // All inputs/outputs. Correspond to physical blocks and pixels within the circuit.
    /**
     *   INPUT/OUTPUT points are numbered clockwise:
     *   ONE  #    TWO   #   FOUR
     *    0   |    0 1   |    0 1            N
     *  3 o 1 |  5 o o 2 |  7 o o 2        W X E
     *    2   |    4 3   |  6 o o 3          S
     *        |          |    5 4
     *        |    0     |
     *        |  5 o 1   |
     *        |  4 o 2   |
     *        |    3     |
     */
    List<Integer> inputs = new ArrayList<>();
    List<Integer> outputs = new ArrayList<>();

    // Outputs of the design. Possible values: 5, 37, 45, 77 (Analogous to 0, 3, 1, 2)
    List<Integer> designOutput = new ArrayList<>();
    // Temp info.
    // List of wires that will be rendered in the next visual render.
    List<Integer> toRender = new ArrayList<>();
    // All interactions involved with this circuit. All changes will be done to ALL current interactions.
    List<Interaction> interactions = new ArrayList<>();

    GuiWallInstance ginstance;

    // State of the circuit.
    Boolean overloaded = false;

    public Circuit(Location loc, int size, String uuid, HashMap<Integer, Object> design, int number){
        LOC = loc;
        owneruuid = uuid;
        this.size = size;
        num = number;
        if(design == null){
            this.design = new HashMap<>();
        } else {
            this.design = design;
        }

        GuiWallInstance instance = GuiWallManager.getInstance().getActiveInstance(loc);

        if(instance != null){
            ginstance = instance;
        } else {
            ginstance = AGUIInstances.getInstance().placeGUI(loc,this, true);
        }
        GUItronics.getInstance().bl.checkCircuitPuts(this);
    }
    
    // Getters
    public Location getLocation(){ return LOC; }

    public List<Location> getLocations(){

        List<Location> locations = new ArrayList<>();

        locations.add(LOC);

        if(size == 1){
            locations.add(LOC.add(1,0,0));
        }
        if(size == 2){
            locations.add(LOC.add(0,0,1));
        }
        if(size == 3){
            locations.add(LOC.add(1,0,0));
            locations.add(LOC.add(0,0,1));
            locations.add(LOC.add(-1,0,1));
        }

        return locations;
    }

    public GuiWallInstance getGuiWallInstance(){ return ginstance; }

    public HashMap<Location, Integer> getPutsLocations(){

        HashMap<Location, Integer> locs = new HashMap<>();
        locs.put(getAuxLoc().add(0,-1,-1), 0);
        switch (size){
            case 0:
                locs.put(getAuxLoc().add(1,-1,0), 1);
                locs.put(getAuxLoc().add(0,-1,1), 2);
                locs.put(getAuxLoc().add(-1,-1,0), 3);
                break;
            case 1:
                locs.put(getAuxLoc().add(1,-1,-1), 1);
                locs.put(getAuxLoc().add(2,-1,0), 2);
                locs.put(getAuxLoc().add(1,-1,1), 3);
                locs.put(getAuxLoc().add(0,-1,1), 4);
                locs.put(getAuxLoc().add(-1,-1,0), 5);
                break;
            case 2:
                locs.put(getAuxLoc().add(1,-1,0), 1);
                locs.put(getAuxLoc().add(1,-1,1), 2);
                locs.put(getAuxLoc().add(0,-1,2), 3);
                locs.put(getAuxLoc().add(-1,-1,1), 4);
                locs.put(getAuxLoc().add(-1,-1,0), 5);
                break;
            case 3:
                locs.put(getAuxLoc().add(1,-1,-1), 1);
                locs.put(getAuxLoc().add(2,-1,0), 2);
                locs.put(getAuxLoc().add(2,-1,-1), 3);
                locs.put(getAuxLoc().add(1,-1,-2), 4);
                locs.put(getAuxLoc().add(0,-1,-2), 5);
                locs.put(getAuxLoc().add(-1,-1,1), 6);
                locs.put(getAuxLoc().add(-1,-1,0), 7);
                break;
        }
        return locs;
    }

    public int getPutFromLoc(Location loc){
        return getPutsLocations().get(loc);
    }

    private Location getAuxLoc(){
        return new Location(LOC.getWorld(), LOC.getX(), LOC.getY(), LOC.getZ());
    }

    public int getSize(){ return size; }

    public int getNum(){ return num; }

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
            case RESISTOR:
                Resistor resistor = (Resistor) design.get(pos);
                if(powered) return resistor.getPoweredColor();
                return resistor.getBasicColor();
            case INVERTER:
                Inverter inverter = (Inverter) design.get(pos);
                if(powered) return inverter.getPoweredColor();
                return inverter.getBasicColor();
        }
        return null;
    }

    public EComponent getEComponent(Object ElectroComponent){

        if(ElectroComponent instanceof Wire){
            return ((Wire) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof Delayer){
            return ((Delayer) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof Diode){
            return ((Diode) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof Inverter){
            return ((Inverter) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof Resistor){
            return ((Resistor) ElectroComponent).getEComponent();
        }
        return null;
    }

    public boolean getPoweredState(Object pos){

        if(getEComponent(design.get(pos)) == null) return false;
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
            case INVERTER:
                Inverter inverter = (Inverter) design.get(pos);
                return inverter.isPowered();
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
    }

    public void addOutput(int output){
        if(!outputs.contains(output)) outputs.add(output);
    }

    public void removePut(Object value){
        if(inputs.contains(value)) inputs.remove(value);
        if(outputs.contains(value)) outputs.remove(value);
        updatePuts();
    }

    public void addDesignOutput(int signal){
        designOutput.add(signal);
    }

    public void removeDesignOutput(int signal){
        if(designOutput.contains(signal)) designOutput.remove(signal);
    }

    public void updateRender(boolean newRender){

        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Tries to render.");
        if(overloaded) return;

        if(newRender){
            toRender.clear();
            CircuitRenderer.getInstance().render(this);
            CircuitRenderer.getInstance().outputRedstone(designOutput, this);
        }

        setDesign(false);

        for(Interaction i : interactions){
            if(i != null){
                for(int j : toRender){
                    updateRect(design.get(j), i, j, true);
                }
            }
        }
    }

    public void updatePuts(){

        for(Interaction i : interactions) {

            for (int k : Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)) {
                if (i.getComponentTree().locate(k + "t") != null)
                    i.getComponentTree().locate(k + "t").setHidden(!inputs.contains(k));
                if (i.getComponentTree().locate(k + "f") != null)
                    i.getComponentTree().locate(k + "f").setHidden(!outputs.contains(k));

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

    // Direction: INT corresponding to clockwise change (Direction). (0-3)

    public void expand(int direction){

        if(size == 0){
            switch (direction){
                case 0:
                    LOC.add(0,0,-1); break;
                case 3:
                    LOC.add(-1,0,0); break;

                case 1:
                    LOC.add(1,-1,0).getBlock().setType(Material.SCULK_CATALYST); break;
                case 2:
                    LOC.add(0,-1,1).getBlock().setType(Material.SCULK_CATALYST); break;

            }
        } else if(size == 1){
            if(direction == 0) { LOC.add(0, 0, -1); }

            LOC.add(0,0,1).getBlock().setType(Material.SCULK_CATALYST);
            LOC.add(1,0,1).getBlock().setType(Material.SCULK_CATALYST);

        } else if(size == 2){
            if(direction == 3) { LOC.add(-1, 0, 0); }

            LOC.add(0,0,1).getBlock().setType(Material.SCULK_CATALYST);
            LOC.add(1,0,1).getBlock().setType(Material.SCULK_CATALYST);
        }

        updateCircuit();
    }

    public void updateCircuit(){
        ginstance = AGUIInstances.getInstance().placeGUI(LOC,this, true);
        Data.getInstance().updateCircuit(this);
    }

    public List<Interaction> getInteractions(){ return interactions; }

    public void setOverloaded(){ overloaded = true; }

    public void unsetOverloaded(){ overloaded = false; }

}
